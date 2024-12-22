package org.example.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.example.client.pojo.dto.UpdateUserDTO;
import org.example.client.pojo.dto.UserLoginDTO;
import org.example.client.pojo.dto.UserRegisterDTO;
import org.example.client.entity.User;
import org.example.client.pojo.vo.UserVO;

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
     * 根据时间戳获取redis缓存中的验证码
     *
     * @param timestamp 时间戳
     * @return 验证码
     */
    String getCodeCache(String timestamp);

    /**
     * 创建动态图形验证码，并以时间戳作为key将验证码缓存到redis中
     *
     * @param timestamp 时间戳
     * @param response  HTTP响应
     * @return 验证码
     */
    String createGifCaptcha(String timestamp, HttpServletResponse response);

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录时传递的数据模型
     * @param code          redis缓存中的验证码
     * @return 登录用户信息
     */
    UserVO login(UserLoginDTO userLoginDTO, String code);

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册时传递的数据模型
     */
    void register(UserRegisterDTO userRegisterDTO);

    /**
     * 更改用户信息
     *
     * @param updateUserDTO 更改用户信息时传递的数据模型
     * @param id            用户ID
     */
    void updateUser(UpdateUserDTO updateUserDTO, String id);
}
