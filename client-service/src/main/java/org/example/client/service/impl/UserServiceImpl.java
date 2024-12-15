package org.example.client.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.context.UserContext;
import org.example.client.entity.User;
import org.example.client.mapper.UserMapper;
import org.example.client.pojo.dto.UpdateUserDTO;
import org.example.client.pojo.dto.UserLoginDTO;
import org.example.client.pojo.dto.UserRegisterDTO;
import org.example.client.pojo.vo.UserVO;
import org.example.client.service.IUserService;
import org.example.common.api.client.CommonClient;
import org.example.common.constant.AccountStatusConstant;
import org.example.common.constant.ClaimConstant;
import org.example.common.constant.GenderConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.*;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    private final UserMapper userMapper;
    private final CommonClient commonClient;
    // 电子邮箱验证正则表达式
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    @Override
    public UserVO login(UserLoginDTO userLoginDTO) {
        String account = userLoginDTO.getAccount();
        String password = userLoginDTO.getPassword();
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>()
                .eq("account", account);
        if (!userMapper.exists(queryWrapper1)) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 查询密码是否正确
        QueryWrapper<User> queryWrapper2 = new QueryWrapper<User>()
                .eq("account", account)
                .eq("password", password);
        User user = userMapper.selectOne(queryWrapper2);
        if (Objects.isNull(user)) {
            throw new NotFoundException(MessageConstant.PASSWORD_ERROR);
        }
        // 判断该用户账号是否被禁用
        if (user.getStatus().equals(AccountStatusConstant.DISABLE)) {
            log.info("[log] 该用户账号被禁用 status: {}", user.getStatus());
            throw new NotAllowedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 构建用户信息载荷
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put(ClaimConstant.CLIENT_ID, user.getId());
        // 成功获取到登录用户信息后，远程调用服务，生成JWT
        String token = commonClient.createToken(userInfo);

        // 构建UserVO
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setToken(token);

        return userVO;
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        String gender = userRegisterDTO.getGender();
        String phone = userRegisterDTO.getPhone();
        String email = userRegisterDTO.getEmail();
        // 查询账号是否已存在
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>()
                .eq("account", userRegisterDTO.getAccount());
        if (userMapper.exists(queryWrapper1)) {
            throw new AlreadyExistsException(MessageConstant.USERNAME_ALREADY_EXISTS);
        }
        // 检查参数是否合法
        if (gender != null) {
            if (!(gender.equals(GenderConstant.MALE) || gender.equals(GenderConstant.FEMALE))) {
                log.info("[log] 参数检查不通过 gender: {}, msg: {}", gender, MessageConstant.INVALID_GENDER);
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
        BeanUtils.copyProperties(userRegisterDTO, user);
        // 新增用户
        userMapper.insert(user);
    }

    @Override
    public void updateUser(UpdateUserDTO updateUserDTO) {
        long userId = UserContext.getUserId();
        String name = updateUserDTO.getName();
        String account = updateUserDTO.getAccount();
        String gender = updateUserDTO.getGender();
        String phone = updateUserDTO.getPhone();
        String email = updateUserDTO.getEmail();
        // 检查账号是否已存在
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>()
                .eq("account", account)
                .ne("id", userId);
        if (userMapper.exists(queryWrapper1)) {
            throw new AlreadyExistsException(MessageConstant.USERNAME_ALREADY_EXISTS);
        }
        // 检查参数是否合法
        if (StrUtil.isNotBlank(name)) {
            if (name.length() < 2 || name.length() > 8) {
                log.info("[log] 参数检查不通过 name: {}, msg: {}", phone, MessageConstant.INVALID_USER_NAME);
                throw new CheckException(MessageConstant.INVALID_USER_NAME);
            }
        }
        if (gender != null) {
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
            QueryWrapper<User> queryWrapper2 = new QueryWrapper<User>()
                    .eq("phone", phone)
                    .ne("id", userId);
            if (userMapper.exists(queryWrapper2)) {
                throw new AlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
            }
        }
        if (email != null) {
            if (!ReUtil.isMatch(EMAIL_REGEX, email)) {
                log.info("[log] 参数检查不通过 email: {}, msg: {}", email, MessageConstant.INVALID_EMAIL);
                throw new CheckException(MessageConstant.INVALID_EMAIL);
            }
            // 检查邮箱是否已存在
            QueryWrapper<User> queryWrapper3 = new QueryWrapper<User>()
                    .eq("email", email)
                    .ne("id", userId);
            if (userMapper.exists(queryWrapper3)) {
                throw new AlreadyExistsException(MessageConstant.EMAIL_ALREADY_EXISTS);
            }
        }
        // 构建用户对象
        User user = new User();
        BeanUtils.copyProperties(updateUserDTO, user);
        user.setId(userId);
        // 更改用户信息
        try {
            userMapper.updateById(user);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 更改用户信息失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.UPDATE_FIELD_NOT_SET);
            throw new NullUpdateException(MessageConstant.UPDATE_FIELD_NOT_SET);
        }
    }

}
