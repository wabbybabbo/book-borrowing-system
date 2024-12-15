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
     * @param pageQuery 分页查询时传递的数据模型
     * @return 用户信息列表
     */
    PageResult<User> pageQuery(PageQuery pageQuery);

    /**
     * 新建用户信息
     *
     * @param file          用户头像图片文件
     * @param createUserDTO 新建用户信息时传递的数据模型
     */
    void createUser(MultipartFile file, CreateUserDTO createUserDTO);

    /**
     * 禁用用户账号
     *
     * @param id 用户ID
     */
    void disableAccount(Long id);

    /**
     * 解禁用户账号
     *
     * @param id 用户ID
     */
    void enableAccount(Long id);

    /**
     * 批量禁用用户账号
     *
     * @param ids 用户ID列表
     */
    void batchDisableAccount(List<Long> ids);

    /**
     * 批量解禁用户账号
     *
     * @param ids 用户ID列表
     */
    void batchEnableAccount(List<Long> ids);

}
