package org.example.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.User;
import org.example.admin.mapper.UserMapper;
import org.example.admin.pojo.dto.CreateUserDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.service.IUserService;
import org.example.common.api.client.CommonClient;
import org.example.common.constant.AccountStatusConstant;
import org.example.common.constant.GenderConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.AlreadyExistsException;
import org.example.common.exception.CheckException;
import org.example.common.exception.NotFoundException;
import org.example.common.result.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final CommonClient commonClient;
    private final UserMapper userMapper;
    // 电子邮箱验证正则表达式
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    @Override
    public PageResult<User> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<User> page = pageQuery.toMpPage();
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        List<String> filterConditions = pageQuery.getFilterConditions();
        log.info("[log] 用户信息分页查询条件 filterConditions: {}", filterConditions);
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
            userMapper.selectPage(page, queryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 用户信息分页查询失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.FIELD_NOT_FOUND);
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }

        return PageResult.<User>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(page.getRecords())
                .build();
    }

    @Override
    public void createUser(MultipartFile file, CreateUserDTO createUserDTO) {
        String gender = createUserDTO.getGender();
        String phone = createUserDTO.getPhone();
        String email = createUserDTO.getEmail();
        // 查询账号是否已存在
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>()
                .eq("account", createUserDTO.getAccount());
        if (userMapper.exists(queryWrapper1)) {
            throw new AlreadyExistsException(MessageConstant.USERNAME_ALREADY_EXISTS);
        }
        // 检查参数是否合法
        if (gender != null) {
            if (!(gender.equals(GenderConstant.MALE) || gender.equals(GenderConstant.FEMALE))) {
                log.info("[log] 参数检查不通过 gender: {}, msg: {}", phone, MessageConstant.INVALID_GENDER);
                throw new CheckException(MessageConstant.INVALID_GENDER);
            }
        }
        if (phone != null) {
            if (phone.length() != 11) {
                log.info("[log] 参数检查不通过 phone: {}, msg: {}", phone, MessageConstant.INVALID_PHONE);
                throw new CheckException(MessageConstant.INVALID_PHONE);
            }
            // 查询电话号码是否已存在
            QueryWrapper<User> queryWrapper2 = new QueryWrapper<User>()
                    .eq("phone", phone);
            if (userMapper.exists(queryWrapper2)) {
                throw new AlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
            }
        }
        if (email != null) {
            if (!ReUtil.isMatch(EMAIL_REGEX, email)) {
                log.info("[log] 参数检查不通过 email: {}, msg: {}", email, MessageConstant.INVALID_EMAIL);
                throw new CheckException(MessageConstant.INVALID_EMAIL);
            }
            // 查询电子邮箱是否已存在
            QueryWrapper<User> queryWrapper3 = new QueryWrapper<User>()
                    .eq("email", email);
            if (userMapper.exists(queryWrapper3)) {
                throw new AlreadyExistsException(MessageConstant.EMAIL_ALREADY_EXISTS);
            }
        }
        // 构建用户对象
        User user = new User();
        BeanUtils.copyProperties(createUserDTO, user);
        if (Objects.nonNull(file)) {
            // 上传书籍封面图片文件
            String url = commonClient.upload(file);
            user.setImgUrl(url);
        }
        // 新增用户信息
        userMapper.insert(user);
    }

    @Override
    public void disableAccount(Long id) {
        // 禁用用户账号
        User user = new User();
        user.setId(id);
        user.setStatus(AccountStatusConstant.DISABLE);
        userMapper.updateById(user);
    }

    @Override
    public void enableAccount(Long id) {
        // 解禁用户账号
        User user = new User();
        user.setId(id);
        user.setStatus(AccountStatusConstant.ENABLE);
        userMapper.updateById(user);
    }

    @Override
    public void batchDisableAccount(List<Long> ids) {
        // 批量禁用用户账号
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>()
                .set("status", AccountStatusConstant.DISABLE)
                .in("id", ids);
        userMapper.update(updateWrapper);
    }

    @Override
    public void batchEnableAccount(List<Long> ids) {
        // 批量解禁用户账号
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>()
                .set("status", AccountStatusConstant.ENABLE)
                .in("id", ids);
        userMapper.update(updateWrapper);
    }

}
