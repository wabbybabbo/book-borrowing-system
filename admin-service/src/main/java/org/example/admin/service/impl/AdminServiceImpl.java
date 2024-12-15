package org.example.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.context.AdminContext;
import org.example.admin.entity.Admin;
import org.example.admin.mapper.AdminMapper;
import org.example.admin.pojo.dto.AdminLoginDTO;
import org.example.admin.pojo.dto.CreateAdminDTO;
import org.example.admin.pojo.dto.UpdateAdminDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.AdminLoginVO;
import org.example.admin.service.IAdminService;
import org.example.common.api.client.CommonClient;
import org.example.common.constant.AccountStatusConstant;
import org.example.common.constant.ClaimConstant;
import org.example.common.constant.GenderConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.AlreadyExistsException;
import org.example.common.exception.CheckException;
import org.example.common.exception.NotFoundException;
import org.example.common.exception.NullUpdateException;
import org.example.common.result.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    // 电子邮箱验证正则表达式
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    @Override
    public AdminLoginVO login(AdminLoginDTO adminLoginDTO) {
        String account = adminLoginDTO.getAccount();
        String password = adminLoginDTO.getPassword();
        // 查询登录的管理员信息是否存在
        QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<Admin>()
                .eq("account", account);
        if (!adminMapper.exists(queryWrapper1)) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 查询密码是否正确
        QueryWrapper<Admin> queryWrapper2 = new QueryWrapper<Admin>()
                .eq("account", account)
                .eq("password", password);
        Admin admin = adminMapper.selectOne(queryWrapper2);
        if (Objects.isNull(admin)) {
            throw new NotFoundException(MessageConstant.PASSWORD_ERROR);
        }
        // 判断该管理员账号是否被禁用
        if (admin.getStatus().equals(AccountStatusConstant.DISABLE)) {
            log.info("[log] 该管理员账号被禁用 status: {}", admin.getStatus());
            throw new CheckException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 构建管理员信息载荷
        HashMap<String, Object> adminInfo = new HashMap<>();
        adminInfo.put(ClaimConstant.CLIENT_ID, admin.getId());
        // 成功获取到登录管理员信息后，远程调用服务，生成JWT
        String token = commonClient.createToken(adminInfo);

        // 构建AdminLoginVO
        AdminLoginVO adminLoginVO = new AdminLoginVO();
        BeanUtils.copyProperties(admin, adminLoginVO);
        adminLoginVO.setToken(token);

        return adminLoginVO;
    }

    @Override
    public PageResult<Admin> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<Admin> page = pageQuery.toMpPage();
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>();
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
        String phone = createAdminDTO.getPhone();
        String email = createAdminDTO.getEmail();
        // 查询管理员账号是否已存在
        QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<Admin>()
                .eq("account", createAdminDTO.getAccount());
        if (adminMapper.exists(queryWrapper1)) {
            throw new AlreadyExistsException(MessageConstant.USERNAME_ALREADY_EXISTS);
        }
        // 检查参数是否合法
        if (phone != null) {
            if (phone.length() != 11) {
                log.info("[log] 参数检查不通过 phone: {}, msg: {}", phone, MessageConstant.INVALID_PHONE);
                throw new CheckException(MessageConstant.INVALID_PHONE);
            }
            // 查询电话号码是否已存在
            QueryWrapper<Admin> queryWrapper2 = new QueryWrapper<Admin>()
                    .eq("phone", phone);
            if (adminMapper.exists(queryWrapper2)) {
                throw new AlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
            }
        }
        if (email != null) {
            if (!ReUtil.isMatch(EMAIL_REGEX, email)) {
                log.info("[log] 参数检查不通过 email: {}, msg: {}", phone, MessageConstant.INVALID_EMAIL);
                throw new CheckException(MessageConstant.INVALID_EMAIL);
            }
            // 查询电子邮箱是否已存在
            QueryWrapper<Admin> queryWrapper3 = new QueryWrapper<Admin>()
                    .eq("email", email);
            if (adminMapper.exists(queryWrapper3)) {
                throw new AlreadyExistsException(MessageConstant.EMAIL_ALREADY_EXISTS);
            }
        }
        // 构建管理员对象
        Admin admin = new Admin();
        BeanUtils.copyProperties(createAdminDTO, admin);
        if (Objects.nonNull(file)) {
            // 上传书籍封面图片文件
            String url = commonClient.upload(file);
            admin.setImgUrl(url);
        }
        // 新增管理员信息
        adminMapper.insert(admin);
    }

    @Override
    public void updateAdmin(UpdateAdminDTO updateAdminDTO) {
        long adminId = AdminContext.getAdminId();
        String name = updateAdminDTO.getName();
        String account = updateAdminDTO.getAccount();
        String gender = updateAdminDTO.getGender();
        String phone = updateAdminDTO.getPhone();
        String email = updateAdminDTO.getEmail();
        // 检查账号是否已存在
        QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<Admin>()
                .eq("account", account)
                .ne("id", adminId);
        if (adminMapper.exists(queryWrapper1)) {
            throw new AlreadyExistsException(MessageConstant.USERNAME_ALREADY_EXISTS);
        }
        // 检查参数是否合法
        if (StrUtil.isNotBlank(name)) {
            if (name.length() < 2 || name.length() > 8) {
                log.info("[log] 参数检查不通过 name: {}, msg: {}", phone, MessageConstant.INVALID_ADMIN_NAME);
                throw new CheckException(MessageConstant.INVALID_ADMIN_NAME);
            }
        }
        if (StrUtil.isNotBlank(gender)) {
            if (!(gender.equals(GenderConstant.MALE) || gender.equals(GenderConstant.FEMALE))) {
                log.info("[log] 参数检查不通过 gender: {}, msg: {}", gender, MessageConstant.INVALID_GENDER);
                throw new CheckException(MessageConstant.INVALID_GENDER);
            }
        }
        if (StrUtil.isNotBlank(phone)) {
            if (phone.length() != 11) {
                log.info("[log] 参数检查不通过 phone: {}, msg: {}", phone, MessageConstant.INVALID_PHONE);
                throw new CheckException(MessageConstant.INVALID_PHONE);
            }
            // 检查电话号码是否已存在
            QueryWrapper<Admin> queryWrapper2 = new QueryWrapper<Admin>()
                    .eq("phone", phone)
                    .ne("id", adminId);
            if (adminMapper.exists(queryWrapper2)) {
                throw new AlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
            }
        }
        if (StrUtil.isNotBlank(email)) {
            if (!ReUtil.isMatch(EMAIL_REGEX, email)) {
                log.info("[log] 参数检查不通过 email: {}, msg: {}", email, MessageConstant.INVALID_EMAIL);
                throw new CheckException(MessageConstant.INVALID_EMAIL);
            }
            // 检查邮箱是否已存在
            QueryWrapper<Admin> queryWrapper3 = new QueryWrapper<Admin>()
                    .eq("email", email)
                    .ne("id", adminId);
            if (adminMapper.exists(queryWrapper3)) {
                throw new AlreadyExistsException(MessageConstant.EMAIL_ALREADY_EXISTS);
            }
        }
        // 构建管理员对象
        Admin admin = new Admin();
        BeanUtils.copyProperties(updateAdminDTO, admin);
        admin.setId(adminId);
        // 更改管理员信息
        try {
            adminMapper.updateById(admin);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 更改管理员信息失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.UPDATE_FIELD_NOT_SET);
            throw new NullUpdateException(MessageConstant.UPDATE_FIELD_NOT_SET);
        }
    }

    @Override
    public void disableAccount(Long id) {
        // 禁用管理员账号
        Admin admin = new Admin();
        admin.setId(id);
        admin.setStatus(AccountStatusConstant.DISABLE);
        adminMapper.updateById(admin);
    }

    @Override
    public void enableAccount(Long id) {
        // 解禁管理员账号
        Admin admin = new Admin();
        admin.setId(id);
        admin.setStatus(AccountStatusConstant.ENABLE);
        adminMapper.updateById(admin);
    }

    @Override
    public void batchDisableAccount(List<Long> ids) {
        // 批量禁用管理员账号
        UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<Admin>()
                .set("status", AccountStatusConstant.DISABLE)
                .in("id", ids);
        adminMapper.update(updateWrapper);
    }

    @Override
    public void batchEnableAccount(List<Long> ids) {
        // 批量解禁管理员账号
        UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<Admin>()
                .set("status", AccountStatusConstant.ENABLE)
                .in("id", ids);
        adminMapper.update(updateWrapper);
    }

}
