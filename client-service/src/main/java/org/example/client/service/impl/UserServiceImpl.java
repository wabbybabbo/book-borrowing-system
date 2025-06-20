package org.example.client.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.entity.User;
import org.example.client.mapper.UserMapper;
import org.example.client.pojo.dto.UpdateEmailDTO;
import org.example.client.pojo.dto.UpdateUserDTO;
import org.example.client.pojo.dto.UserLoginDTO;
import org.example.client.pojo.dto.UserRegisterDTO;
import org.example.client.pojo.vo.UserVO;
import org.example.client.service.IUserService;
import org.example.common.client.CommonClient;
import org.example.common.constant.ClaimConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RabbitMQConstant;
import org.example.common.exception.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    private final RedisTemplate<String, String> redisTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final CodeGenerator randomCodeGenerator = new RandomGenerator(RandomUtil.BASE_NUMBER, 6);
    private final CodeGenerator mathCodeGenerator = new MathGenerator(1);

    @Override
    public void createGifCaptcha(String timestamp, HttpServletResponse response) {
        // 定义动态图形验证码的长、宽
        GifCaptcha gifCaptcha = CaptchaUtil.createGifCaptcha(120, 40);
        // 自定义验证码内容为1位数的四则运算方式
        gifCaptcha.setGenerator(mathCodeGenerator);
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
        // 将验证码放到缓存中，并设置过期时间为5分钟
        redisTemplate.opsForValue().set("codeCache:" + timestamp, gifCaptcha.getCode(), 5, TimeUnit.MINUTES);
    }

    @Override
    public UserVO login(UserLoginDTO userLoginDTO) {
        // 根据时间戳获取redis缓存中的验证码
        String timestamp = userLoginDTO.getTimestamp();
        String code = redisTemplate.opsForValue().get("codeCache:" + timestamp);
        if (Objects.isNull(code)) {
            log.info("获取redis缓存中的验证码失败 timestamp: {}, msg: {}", timestamp, MessageConstant.CODE_NOT_FOUND);
            throw new NotFoundException(MessageConstant.CODE_NOT_FOUND);
        }
        // 验证码校验
        String userInputCode = userLoginDTO.getCode();
        if (!mathCodeGenerator.verify(code, userInputCode)) {
            log.info("验证码校验不通过 code: {}, userInputCode: {}, msg: {}", code, userInputCode, MessageConstant.CODE_ERROR);
            throw new CheckException(MessageConstant.CODE_ERROR);
        }
        // 查询账号是否存在
        String account = userLoginDTO.getAccount();
        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<User>()
                .eq(User::getAccount, account);
        if (!userMapper.exists(queryWrapper1)) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 查询密码是否正确
        String password = userLoginDTO.getPassword();
        LambdaQueryWrapper<User> queryWrapper2 = new LambdaQueryWrapper<User>()
                .eq(User::getAccount, account)
                .eq(User::getPassword, password);
        User user = userMapper.selectOne(queryWrapper2);
        if (Objects.isNull(user)) {
            throw new NotFoundException(MessageConstant.PASSWORD_ERROR);
        }
        // 判断该用户账号是否被禁用
        if (!user.getStatus()) {
            log.info("[log] 该用户账号被禁用 status: false");
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
    public void sendCaptcha2Email4Register(String email, Long timeout) {
        // 查询该邮箱是否已被使用
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email);
        if (userMapper.exists(queryWrapper)) {
            throw new AlreadyExistsException(MessageConstant.EMAIL_ALREADY_EXISTS);
        }

        String code = randomCodeGenerator.generate();
        String content = "您正在注册账号，验证码是："
                + "<b style=\"color: rgb(92, 128, 128); text-decoration: rgb(92, 128, 128) underline\">"
                + code + "</b>（请勿泄露），此验证码" + timeout + "分钟内有效。<br><br>如非本人操作，请忽略。";
        // 将验证码存到redis缓存中，并设置过期时间
        redisTemplate.opsForValue().set("codeCache:" + email, code, timeout, TimeUnit.MINUTES);
        // 发送验证码到用户邮箱
        MailUtil.send(email, "【书店借阅平台】邮箱验证码", content, true);
    }

    @Override
    public void sendCaptcha2Email4UpdateEmail(String email, Long timeout) {
        // 查询该邮箱是否已被使用
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email);
        if (userMapper.exists(queryWrapper)) {
            throw new AlreadyExistsException(MessageConstant.EMAIL_ALREADY_EXISTS);
        }

        String code = randomCodeGenerator.generate();
        String content = "您正在换绑邮箱，验证码是："
                + "<b style=\"color: rgb(92, 128, 128); text-decoration: rgb(92, 128, 128) underline\">"
                + code + "</b>（请勿泄露），此验证码" + timeout + "分钟内有效。<br><br>如非本人操作，请忽略。";
        // 将验证码存到redis缓存中，并设置过期时间
        redisTemplate.opsForValue().set("codeCache:" + email, code, timeout, TimeUnit.MINUTES);
        // 发送验证码到用户邮箱
        MailUtil.send(email, "【书店借阅平台】邮箱验证码", content, true);
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        // 根据邮箱地址获取redis缓存中的验证码
        String email = userRegisterDTO.getEmail();
        String code = redisTemplate.opsForValue().get("codeCache:" + email);
        if (Objects.isNull(code)) {
            log.info("获取redis缓存中的验证码失败 email: {}, msg: {}", email, MessageConstant.CODE_NOT_FOUND);
            throw new NotFoundException(MessageConstant.CODE_NOT_FOUND);
        }
        // 验证码校验
        String userInputCode = userRegisterDTO.getCode();
        if (!randomCodeGenerator.verify(code, userInputCode)) {
            log.info("验证码校验不通过 code: {}, userInputCode: {}, msg: {}", code, userInputCode, MessageConstant.CODE_ERROR);
            throw new CheckException(MessageConstant.CODE_ERROR);
        }
        // 查询账号是否已存在
        String account = userRegisterDTO.getAccount();
        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<User>()
                .eq(User::getAccount, account);
        if (userMapper.exists(queryWrapper1)) {
            throw new AlreadyExistsException(MessageConstant.ACCOUNT_ALREADY_EXISTS);
        }

        String phone = userRegisterDTO.getPhone();
        if (Objects.nonNull(phone)) {
            // 查询电话号码是否已存在
            LambdaQueryWrapper<User> queryWrapper2 = new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, phone);
            if (userMapper.exists(queryWrapper2)) {
                throw new AlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
            }
        }

        // 构建用户对象
        User user = new User();
        BeanUtil.copyProperties(userRegisterDTO, user);
        // 新增用户信息
        userMapper.insert(user);

        // 查询新增的用户信息的ID
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .select(User::getId)
                .eq(User::getAccount, user.getAccount());
        String userId = userMapper.selectOne(queryWrapper).getId();

        //构建RabbitAdmin对象，用于管理（声明或删除）RabbitMQ的队列、交换机和绑定关系
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        //构建队列对象
        Queue noticeQueue = new Queue(RabbitMQConstant.NOTICE_QUEUE + userId);
        Queue reminderQueue = new Queue(RabbitMQConstant.REMINDER_QUEUE + userId);
        //构建交换机对象
        FanoutExchange noticeFanoutExchange = new FanoutExchange(RabbitMQConstant.NOTICE_FANOUT_EXCHANGE);
        DirectExchange reminderDirectExchange = new DirectExchange(RabbitMQConstant.REMINDER_DIRECT_EXCHANGE);
        //构建绑定关系对象
        Binding noticeBinding = BindingBuilder.bind(noticeQueue).to(noticeFanoutExchange);
        Binding reminderBinding = BindingBuilder.bind(reminderQueue).to(reminderDirectExchange).with(userId);
        //声明队列，即如果队列不存在则创建它
        rabbitAdmin.declareQueue(noticeQueue);
        rabbitAdmin.declareQueue(reminderQueue);
        //声明交换机，即如果交换机不存在则创建它
        rabbitAdmin.declareExchange(noticeFanoutExchange);
        rabbitAdmin.declareExchange(reminderDirectExchange);
        //声明绑定关系，将队列绑定到交换机
        rabbitAdmin.declareBinding(noticeBinding);
        rabbitAdmin.declareBinding(reminderBinding);
    }

    @Override
    public void updateUser(String id, UpdateUserDTO updateUserDTO) {
        // 检查Bean对象中字段是否全空
        if (BeanUtil.isEmpty(updateUserDTO)) {
            throw new MissingValueException(MessageConstant.MISSING_UPDATE_VALUE);
        }

        String account = updateUserDTO.getAccount();
        if (Objects.nonNull(account)) {
            // 检查账号是否已存在
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                    .eq(User::getAccount, account)
                    .ne(User::getId, id);
            if (userMapper.exists(queryWrapper)) {
                throw new AlreadyExistsException(MessageConstant.ACCOUNT_ALREADY_EXISTS);
            }
        }
        String phone = updateUserDTO.getPhone();
        if (Objects.nonNull(phone)) {
            // 检查电话号码是否已存在
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, phone)
                    .ne(User::getId, id);
            if (userMapper.exists(queryWrapper)) {
                throw new AlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
            }
        }
        String email = updateUserDTO.getEmail();
        if (Objects.nonNull(email)) {
            // 检查邮箱是否已存在
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, email)
                    .ne(User::getId, id);
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

    @Override
    public String updateAvatar(String id, MultipartFile file) {
        // 构建用户对象
        User user = new User();
        user.setId(id);
        // 上传头像图片文件
        String url = commonClient.upload(file);
        user.setImgUrl(url);
        // 更改用户信息
        userMapper.updateById(user);

        return url;
    }

    @Override
    public void updateEmail(String id, UpdateEmailDTO updateEmailDTO) {
        // 根据邮箱地址获取redis缓存中的验证码
        String email = updateEmailDTO.getEmail();
        String code = redisTemplate.opsForValue().get("codeCache:" + email);
        if (Objects.isNull(code)) {
            log.info("获取redis缓存中的验证码失败 email: {}, msg: {}", email, MessageConstant.CODE_NOT_FOUND);
            throw new NotFoundException(MessageConstant.CODE_NOT_FOUND);
        }
        // 验证码校验
        String userInputCode = updateEmailDTO.getCode();
        if (!randomCodeGenerator.verify(code, userInputCode)) {
            log.info("验证码校验不通过 code: {}, userInputCode: {}, msg: {}", code, userInputCode, MessageConstant.CODE_ERROR);
            throw new CheckException(MessageConstant.CODE_ERROR);
        }

        // 构建用户对象
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        // 更改邮箱地址
        userMapper.updateById(user);
    }

}
