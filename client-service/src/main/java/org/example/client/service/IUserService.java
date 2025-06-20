package org.example.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.example.client.entity.User;
import org.example.client.pojo.dto.UpdateEmailDTO;
import org.example.client.pojo.dto.UpdateUserDTO;
import org.example.client.pojo.dto.UserLoginDTO;
import org.example.client.pojo.dto.UserRegisterDTO;
import org.example.client.pojo.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
public interface IUserService extends IService<User> {

    /**
     * 发送验证码到邮箱用于用户注册
     *
     * @param email   用户邮箱
     * @param timeout 验证码有效时长（分钟）
     */
    void sendCaptcha2Email4Register(String email, Long timeout);

    /**
     * 发送验证码到邮箱用于换绑邮箱
     *
     * @param email   用户邮箱
     * @param timeout 验证码有效时长（分钟）
     */
    void sendCaptcha2Email4UpdateEmail(String email, Long timeout);

    /**
     * 用户注册
     *
     * @param userRegisterDTO {@link UserRegisterDTO}
     */
    void register(UserRegisterDTO userRegisterDTO);

    /**
     * 创建动态图形验证码，并以时间戳作为key将验证码缓存到redis中
     *
     * @param timestamp 时间戳
     * @param response  HTTP响应
     */
    void createGifCaptcha(String timestamp, HttpServletResponse response);

    /**
     * 用户登录
     *
     * @param userLoginDTO {@link UserLoginDTO}
     * @return {@link UserVO}
     */
    UserVO login(UserLoginDTO userLoginDTO);

    /**
     * 更改用户信息
     *
     * @param id            用户ID
     * @param updateUserDTO {@link UpdateUserDTO}
     */
    void updateUser(String id, UpdateUserDTO updateUserDTO);

    /**
     * 更换用户头像
     *
     * @param id   用户ID
     * @param file 头像图片文件
     * @return 头像图片访问地址
     */
    String updateAvatar(String id, MultipartFile file);

    /**
     * 换绑邮箱
     *
     * @param id             用户ID
     * @param updateEmailDTO {@link UpdateEmailDTO}
     */
    void updateEmail(String id, UpdateEmailDTO updateEmailDTO);

}
