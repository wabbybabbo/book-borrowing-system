package org.example.client.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.entity.User;
import org.example.client.mapper.UserMapper;
import org.example.client.pojo.dto.UpdateUserDTO;
import org.example.client.pojo.dto.UserLoginDTO;
import org.example.client.pojo.dto.UserRegisterDTO;
import org.example.client.pojo.vo.UserVO;
import org.example.client.service.IUserService;
import org.example.common.client.CommonClient;
import org.example.common.constant.AccountStatusConstant;
import org.example.common.constant.ClaimConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.*;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    public UserVO login(UserLoginDTO userLoginDTO, String code) {
        // 验证码校验
        CodeGenerator mathGenerator = new MathGenerator(1);
        String userInputCode = userLoginDTO.getCode();
        if (!mathGenerator.verify(code, userInputCode)) {
            log.info("验证码校验不通过 code: {}, userInputCode: {}, msg: {}", code, userInputCode, MessageConstant.CAPTCHA_ERROR);
            throw new CheckException(MessageConstant.CAPTCHA_ERROR);
        }
        // 查询账号是否存在
        String account = userLoginDTO.getAccount();
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>()
                .eq("account", account);
        if (!userMapper.exists(queryWrapper1)) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 查询密码是否正确
        String password = userLoginDTO.getPassword();
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
        // 远程调用服务，生成JWT
        String token = commonClient.createToken(userInfo);

        // 构建UserVO
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        userVO.setToken(token);

        return userVO;
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        // 查询账号是否已存在
        String account = userRegisterDTO.getAccount();
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>()
                .eq("account", account);
        if (userMapper.exists(queryWrapper1)) {
            throw new AlreadyExistsException(MessageConstant.ACCOUNT_ALREADY_EXISTS);
        }
        // 检查参数值在数据库中的唯一性
        String phone = userRegisterDTO.getPhone();
        if (Objects.nonNull(phone)) {
            // 查询电话号码是否已存在
            QueryWrapper<User> queryWrapper2 = new QueryWrapper<User>()
                    .eq("phone", phone);
            if (userMapper.exists(queryWrapper2)) {
                throw new AlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
            }
        }
        String email = userRegisterDTO.getEmail();
        if (Objects.nonNull(email)) {
            // 查询电子邮箱是否已存在
            QueryWrapper<User> queryWrapper3 = new QueryWrapper<User>()
                    .eq("email", email);
            if (userMapper.exists(queryWrapper3)) {
                throw new AlreadyExistsException(MessageConstant.EMAIL_ALREADY_EXISTS);
            }
        }
        // 构建用户对象
        User user = new User();
        BeanUtil.copyProperties(userRegisterDTO, user);
        // 新增用户信息
        userMapper.insert(user);
    }

    @Override
    public void updateUser(UpdateUserDTO updateUserDTO, String id) {
        // 检查Bean对象中字段是否全空
        if(BeanUtil.isEmpty(updateUserDTO)){
            throw new MissingValueException(MessageConstant.MISSING_UPDATE_VALUE);
        }
        // 检查参数值在数据库中的唯一性
        String account = updateUserDTO.getAccount();
        if (Objects.nonNull(account)) {
            // 检查账号是否已存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                    .eq("account", account)
                    .ne("id", id);
            if (userMapper.exists(queryWrapper)) {
                throw new AlreadyExistsException(MessageConstant.ACCOUNT_ALREADY_EXISTS);
            }
        }
        String phone = updateUserDTO.getPhone();
        if (Objects.nonNull(phone)) {
            // 检查电话号码是否已存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                    .eq("phone", phone)
                    .ne("id", id);
            if (userMapper.exists(queryWrapper)) {
                throw new AlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
            }
        }
        String email = updateUserDTO.getEmail();
        if (Objects.nonNull(email)) {
            // 检查邮箱是否已存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                    .eq("email", email)
                    .ne("id", id);
            if (userMapper.exists(queryWrapper)) {
                throw new AlreadyExistsException(MessageConstant.EMAIL_ALREADY_EXISTS);
            }
        }
        // 构建用户对象
        User user = new User();
        BeanUtil.copyProperties(updateUserDTO, user);
        user.setId(id);
        // 更改用户信息
        userMapper.updateById(user);
    }

}
