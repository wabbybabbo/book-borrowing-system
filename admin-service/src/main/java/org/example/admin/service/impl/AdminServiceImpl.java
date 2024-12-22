package org.example.admin.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.Admin;
import org.example.admin.mapper.AdminMapper;
import org.example.admin.pojo.dto.AdminLoginDTO;
import org.example.admin.pojo.dto.CreateAdminDTO;
import org.example.admin.pojo.dto.UpdateAdminDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.AdminLoginVO;
import org.example.admin.service.IAdminService;
import org.example.common.client.CommonClient;
import org.example.common.constant.AccountStatusConstant;
import org.example.common.constant.ClaimConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.*;
import org.example.common.result.PageResult;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 管理员信息表 服务实现类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-12-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    private final CommonClient commonClient;
    private final AdminMapper adminMapper;

    @Override
    @CachePut(cacheNames = "codeCache", key = "#timestamp") //在方法执行后生效，将方法的返回值放到缓存中
    public String createGifCaptcha(String timestamp, HttpServletResponse response) {
        // 定义动态图形验证码的长、宽
        GifCaptcha gifCaptcha = CaptchaUtil.createGifCaptcha(120, 40);
        // 自定义验证码内容为1位数的四则运算方式
        gifCaptcha.setGenerator(new MathGenerator(1));
        try {
            // 写出到浏览器（Servlet输出流）
            gifCaptcha.write(response.getOutputStream());
            // 关闭流
            response.getOutputStream().close();
        } catch (IOException e) {
            log.error("[log] 获取动态图形验证码失败 IOException {}", e.getMessage());
            throw new ServiceException(MessageConstant.CREATE_CAPTCHA_FAILED);
        }
        // 指示客户端（例如浏览器）不要缓存响应，确保浏览器从服务器获取最新的数据，而不是从缓存中获取过时的数据。
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        // 告诉客户端（例如浏览器）响应包含了GIF图像数据，浏览器会根据这个信息来处理和显示数据
        response.setContentType("image/gif");
        // 将验证码放到缓存中
        return gifCaptcha.getCode();
    }

    @Override
    @Cacheable(cacheNames = "codeCache", key = "#timestamp")
    public String getCodeCache(String timestamp) {
        log.info("获取redis缓存中的验证码失败 timestamp: {}, msg: {}", timestamp, MessageConstant.CAPTCHA_NOT_FOUND);
        throw new NotFoundException(MessageConstant.CAPTCHA_NOT_FOUND);
    }

    @Override
    public AdminLoginVO login(AdminLoginDTO adminLoginDTO, String code) {
        // 验证码校验
        CodeGenerator mathGenerator = new MathGenerator(1);
        String adminInputCode = adminLoginDTO.getCode();
        if (!mathGenerator.verify(code, adminInputCode)) {
            log.info("验证码校验不通过 code: {}, adminInputCode: {}, msg: {}", code, adminInputCode, MessageConstant.CAPTCHA_ERROR);
            throw new CheckException(MessageConstant.CAPTCHA_ERROR);
        }
        // 查询账号是否存在
        String account = adminLoginDTO.getAccount();
        QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<Admin>()
                .eq("account", account);
        if (!adminMapper.exists(queryWrapper1)) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 查询密码是否正确
        String password = adminLoginDTO.getPassword();
        QueryWrapper<Admin> queryWrapper2 = new QueryWrapper<Admin>()
                .eq("account", account)
                .eq("password", password);
        Admin admin = adminMapper.selectOne(queryWrapper2);
        if (Objects.isNull(admin)) {
            throw new NotFoundException(MessageConstant.PASSWORD_ERROR);
        }
        // 判断该账号是否被禁用
        if (admin.getStatus().equals(AccountStatusConstant.DISABLE)) {
            log.info("[log] 该管理员账号被禁用 status: {}, msg: {}", admin.getStatus(), MessageConstant.ACCOUNT_LOCKED);
            throw new CheckException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 构建管理员信息载荷
        HashMap<String, Object> adminInfo = new HashMap<>();
        adminInfo.put(ClaimConstant.CLIENT_ID, admin.getId());
        // 远程调用服务，生成JWT
        String token = commonClient.createToken(adminInfo);

        // 构建AdminLoginVO
        AdminLoginVO adminLoginVO = new AdminLoginVO();
        BeanUtil.copyProperties(admin, adminLoginVO);
        adminLoginVO.setToken(token);

        return adminLoginVO;
    }

    @Override
    public PageResult<Admin> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<Admin> page = pageQuery.toMpPage();
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        List<String> filterConditions = pageQuery.getFilterConditions();
        log.info("[log] filterConditions: {}", filterConditions);
        if (CollUtil.isNotEmpty(filterConditions)) {
            for (String condition : filterConditions) {
                if (condition.contains("=")) {
                    log.info("[log] = condition: {}", condition);
                    String[] pair = condition.split("=");
                    if (pair.length == 2)
                        queryWrapper.eq(pair[0], pair[1]);
                } else if (condition.contains("~")) {
                    log.info("[log] ~ condition: {}", condition);
                    String[] pair = condition.split("~");
                    if (pair.length == 2)
                        queryWrapper.like(pair[0], pair[1]);
                }
            }
        }
        // 分页查询
        try {
            adminMapper.selectPage(page, queryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 管理员信息分页查询失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.FIELD_NOT_FOUND);
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }

        return PageResult.<Admin>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(page.getRecords())
                .build();
    }

    @Override
    public void createAdmin(MultipartFile file, CreateAdminDTO createAdminDTO) {
        // 查询账号是否已存在
        String account = createAdminDTO.getAccount();
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>()
                .eq("account", account);
        if (adminMapper.exists(queryWrapper)) {
            throw new AlreadyExistsException(MessageConstant.ACCOUNT_ALREADY_EXISTS);
        }
        String phone = createAdminDTO.getPhone();
        if (Objects.nonNull(phone)) {
            // 查询电话号码是否已存在
            QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<Admin>()
                    .eq("phone", phone);
            if (adminMapper.exists(queryWrapper1)) {
                throw new AlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
            }
        }
        String email = createAdminDTO.getEmail();
        if (Objects.nonNull(email)) {
            // 查询电子邮箱是否已存在
            QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<Admin>()
                    .eq("email", email);
            if (adminMapper.exists(queryWrapper1)) {
                throw new AlreadyExistsException(MessageConstant.EMAIL_ALREADY_EXISTS);
            }
        }
        // 构建管理员对象
        Admin admin = new Admin();
        BeanUtil.copyProperties(createAdminDTO, admin);
        if (Objects.nonNull(file)) {
            // 上传头像图片文件
            String url = commonClient.upload(file);
            admin.setImgUrl(url);
        }
        // 新增管理员信息
        adminMapper.insert(admin);
    }

    @Override
    public void updateAdmin(UpdateAdminDTO updateAdminDTO, String id) {
        // 检查Bean对象中字段是否全空
        if (BeanUtil.isEmpty(updateAdminDTO)) {
            throw new MissingValueException(MessageConstant.MISSING_UPDATE_VALUE);
        }
        // 验证参数值在数据库中的唯一性
        String account = updateAdminDTO.getAccount();
        if (Objects.nonNull(account)) {
            // 查询账号是否已存在
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>()
                    .eq("account", account)
                    .ne("id", id);
            if (adminMapper.exists(queryWrapper)) {
                throw new AlreadyExistsException(MessageConstant.ACCOUNT_ALREADY_EXISTS);
            }
        }
        String phone = updateAdminDTO.getPhone();
        if (Objects.nonNull(phone)) {
            // 查询电话号码是否已存在
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>()
                    .eq("phone", phone)
                    .ne("id", id);
            if (adminMapper.exists(queryWrapper)) {
                throw new AlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
            }
        }
        String email = updateAdminDTO.getEmail();
        if (Objects.nonNull(email)) {
            // 查询邮箱是否已存在
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>()
                    .eq("email", email)
                    .ne("id", id);
            if (adminMapper.exists(queryWrapper)) {
                throw new AlreadyExistsException(MessageConstant.EMAIL_ALREADY_EXISTS);
            }
        }
        // 构建管理员对象
        Admin admin = new Admin();
        BeanUtil.copyProperties(updateAdminDTO, admin);
        admin.setId(id);
        // 更改管理员信息
        adminMapper.updateById(admin);
    }

    @Override
    public void disableAccount(String id) {
        // 禁用管理员账号
        Admin admin = new Admin();
        admin.setId(id);
        admin.setStatus(AccountStatusConstant.DISABLE);
        adminMapper.updateById(admin);
    }

    @Override
    public void enableAccount(String id) {
        // 解禁管理员账号
        Admin admin = new Admin();
        admin.setId(id);
        admin.setStatus(AccountStatusConstant.ENABLE);
        adminMapper.updateById(admin);
    }

    @Override
    public void batchDisableAccount(List<String> ids) {
        // 批量禁用管理员账号
        UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<Admin>()
                .set("status", AccountStatusConstant.DISABLE)
                .in("id", ids);
        adminMapper.update(updateWrapper);
    }

    @Override
    public void batchEnableAccount(List<String> ids) {
        // 批量解禁管理员账号
        UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<Admin>()
                .set("status", AccountStatusConstant.ENABLE)
                .in("id", ids);
        adminMapper.update(updateWrapper);
    }

}
