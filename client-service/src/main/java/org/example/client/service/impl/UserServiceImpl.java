package org.example.client.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.context.UserContext;
import org.example.client.mapper.UserMapper;
import org.example.client.pojo.dto.UpdateUserDTO;
import org.example.client.pojo.dto.UserLoginDTO;
import org.example.client.pojo.dto.UserRegisterDTO;
import org.example.client.pojo.entity.User;
import org.example.client.pojo.vo.UserVO;
import org.example.client.service.IUserService;
import org.example.common.api.client.CommonClient;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.UserGenderConstant;
import org.example.common.constant.UserInfoConstant;
import org.example.common.constant.UserStatusConstant;
import org.example.common.exception.AlreadyExistsException;
import org.example.common.exception.CheckException;
import org.example.common.exception.NotFoundException;
import org.example.common.exception.NullUpdateException;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wabbybabbo
 * @since 2024-04-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;
    private final CommonClient commonClient;
    // 电子邮箱验证正则表达式
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    // 编译正则表达式模式
    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

    /**
     * 验证电子邮件地址是否符合规范
     *
     * @param email 要验证的电子邮件地址
     * @return 如果电子邮件地址有效，则返回true；否则返回false
     */
    private boolean isValidEmail(String email) {
        Matcher matcher = PATTERN.matcher(email);
        return matcher.matches();
    }

    @Override
    public UserVO login(UserLoginDTO userLoginDTO) {
        String name = userLoginDTO.getName();
        String password = userLoginDTO.getPassword();
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>()
                .eq("name", name);
        if (!userMapper.exists(queryWrapper1)) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 查询密码是否正确
        QueryWrapper<User> queryWrapper2 = new QueryWrapper<User>()
                .eq("name", name)
                .eq("password", password);
        User user = userMapper.selectOne(queryWrapper2);
        if (user == null) {
            throw new NotFoundException(MessageConstant.PASSWORD_ERROR);
        }
        // 判断该用户账号是否被禁用
        if (user.getStatus().equals(UserStatusConstant.DISABLE)) {
            log.info("[log] 该用户账号被禁用");
            throw new CheckException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 构建用户信息载荷
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put(UserInfoConstant.USER_ID, user.getId());
        userInfo.put(UserInfoConstant.USER_ROLE, user.getRole());
        log.info("[log] 用户角色 {}", userInfo.get(UserInfoConstant.USER_ROLE));
        //成功获取到登录用户信息后，远程调用服务，生成jwt令牌
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
        // 检查参数是否合法
        if (null != gender) {
            if (!(gender.equals(UserGenderConstant.MALE) || gender.equals(UserGenderConstant.FEMALE))) {
                throw new CheckException(MessageConstant.INVALID_GENDER);
            }
        }
        if (null != phone && phone.length() != 11) {
            log.info("[log] CheckException: {}", MessageConstant.INVALID_PHONE);
            throw new CheckException(MessageConstant.INVALID_PHONE);
        }
        if (null != email && !isValidEmail(email)) {
            log.info("[log] CheckException: {}", MessageConstant.INVALID_PHONE);
            throw new CheckException(MessageConstant.INVALID_EMAIL);
        }
        // 查询用户名是否被占用
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>()
                .eq("name", userRegisterDTO.getName());
        if (userMapper.exists(queryWrapper1)) {
            throw new AlreadyExistsException(MessageConstant.USERNAME_ALREADY_EXISTS);
        }
        // 查询电话号码是否被占用
        QueryWrapper<User> queryWrapper2 = new QueryWrapper<User>()
                .eq("phone", phone);
        if (userMapper.exists(queryWrapper2)) {
            throw new AlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
        }
        // 查询电子邮箱是否被占用
        QueryWrapper<User> queryWrapper3 = new QueryWrapper<User>()
                .eq("email", email);
        if (userMapper.exists(queryWrapper3)) {
            throw new AlreadyExistsException(MessageConstant.EMAIL_ALREADY_EXISTS);
        }
        // 构建用户对象
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);
        // 新增用户
        userMapper.insert(user);
    }

    @Override
    public void updateUser(UpdateUserDTO updateUserDTO) {
        int userId = UserContext.getUserId();
        String name = updateUserDTO.getName();
        String gender = updateUserDTO.getGender();
        String phone = updateUserDTO.getPhone();
        String email = updateUserDTO.getEmail();
        // 检查参数是否合法
        if (null != gender) {
            if (gender.isEmpty()) {
                throw new CheckException(MessageConstant.INVALID_GENDER);
            }
            if (!(gender.equals(UserGenderConstant.MALE) || gender.equals(UserGenderConstant.FEMALE))) {
                throw new CheckException(MessageConstant.INVALID_GENDER);
            }
        }
        if (null != phone && phone.length() != 11) {
            log.info("[log] CheckException: {}", MessageConstant.INVALID_PHONE);
            throw new CheckException(MessageConstant.INVALID_PHONE);
        }
        if (null != email && !isValidEmail(email)) {
            log.info("[log] CheckException: {}", MessageConstant.INVALID_PHONE);
            throw new CheckException(MessageConstant.INVALID_EMAIL);
        }
        // 检查用户名是否被占用
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>()
                .eq("name", name)
                .ne("id", userId);
        if (userMapper.exists(queryWrapper1)) {
            throw new AlreadyExistsException(MessageConstant.USERNAME_ALREADY_EXISTS);
        }
        // 检查电话号码是否被占用
        QueryWrapper<User> queryWrapper2 = new QueryWrapper<User>()
                .eq("phone", phone)
                .ne("id", userId);
        if (userMapper.exists(queryWrapper2)) {
            throw new AlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
        }
        // 检查邮箱是否被占用
        QueryWrapper<User> queryWrapper3 = new QueryWrapper<User>()
                .eq("email", email)
                .ne("id", userId);
        if (userMapper.exists(queryWrapper3)) {
            throw new AlreadyExistsException(MessageConstant.EMAIL_ALREADY_EXISTS);
        }
        // 构建用户对象
        User user = new User();
        BeanUtils.copyProperties(updateUserDTO, user);
        user.setId(userId);
        // 更改用户信息
        try {
            userMapper.updateById(user);
        } catch (BadSqlGrammarException e) {
            throw new NullUpdateException(MessageConstant.UPDATE_FIELD_NOT_SET);
        }
    }

}
