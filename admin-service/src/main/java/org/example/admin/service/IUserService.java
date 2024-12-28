package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.entity.User;
import org.example.admin.pojo.dto.CreateUserDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.common.result.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
     * 分页查询用户信息
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageResult<User>}
     */
    PageResult<User> pageQuery(PageQuery pageQuery);

    /**
     * 新建用户信息
     *
     * @param file          用户头像图片文件
     * @param createUserDTO {@link CreateUserDTO}
     */
    void createUser(MultipartFile file, CreateUserDTO createUserDTO);

    /**
     * 禁用用户账号
     *
     * @param id 用户ID
     */
    void disableAccount(String id);

    /**
     * 解禁用户账号
     *
     * @param id 用户ID
     */
    void enableAccount(String id);

    /**
     * 批量禁用用户账号
     *
     * @param ids 用户ID列表
     */
    void batchDisableAccount(List<String> ids);

    /**
     * 批量解禁用户账号
     *
     * @param ids 用户ID列表
     */
    void batchEnableAccount(List<String> ids);

}
