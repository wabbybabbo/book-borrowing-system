package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.pojo.dto.CreateUserDTO;
import org.example.admin.pojo.dto.UserLoginDTO;
import org.example.admin.pojo.entity.User;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.UserLoginVO;
import org.example.common.result.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author wabbybabbo
 * @since 2024-04-07
 */
public interface IUserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录时传递的数据模型
     * @return 登录用户信息
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);

    /**
     * 分页查询用户
     *
     * @param pageQuery 分页查询用户时传递的数据模型
     * @return 用户列表
     */
    PageResult<User> pageQuery(PageQuery pageQuery);

    /**
     * 新建用户账号
     *
     * @param file          用户头像图片文件
     * @param createUserDTO 新建用户账号时传递的数据模型
     */
    void createUser(MultipartFile file, CreateUserDTO createUserDTO);

    /**
     * 禁用用户账号
     *
     * @param id 用户ID
     */
    void disableUser(Integer id);

    /**
     * 解禁用户账号
     *
     * @param id 用户ID
     */
    void enableUser(Integer id);

    /**
     * 批量禁用用户账号
     *
     * @param ids 用户ID列表
     */
    void batchDisableUser(List<Integer> ids);

    /**
     * 批量解禁用户账号
     *
     * @param ids 用户ID列表
     */
    void batchEnableUser(List<Integer> ids);

}
