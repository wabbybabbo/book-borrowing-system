package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.entity.Admin;
import org.example.admin.pojo.dto.AdminLoginDTO;
import org.example.admin.pojo.dto.CreateAdminDTO;
import org.example.admin.pojo.dto.UpdateAdminDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.AdminLoginVO;
import org.example.common.result.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 管理员信息表 服务类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-12-14
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 管理员登录
     *
     * @param adminLoginDTO 管理员登录时传递的数据模型
     * @return 登录的管理员信息
     */
    AdminLoginVO login(AdminLoginDTO adminLoginDTO);

    /**
     * 分页查询管理员信息
     *
     * @param pageQuery 分页查询时传递的数据模型
     * @return 管理员列表
     */
    PageResult<Admin> pageQuery(PageQuery pageQuery);

    /**
     * 新建管理员信息
     *
     * @param file           管理员头像图片文件
     * @param createAdminDTO 新建管理员信息时传递的数据模型
     */
    void createAdmin(MultipartFile file, CreateAdminDTO createAdminDTO);

    /**
     * 更改管理员信息
     *
     * @param updateAdminDTO 更改管理员信息时传递的数据模型
     */
    void updateAdmin(UpdateAdminDTO updateAdminDTO);

    /**
     * 禁用管理员账号
     *
     * @param id 管理员ID
     */
    void disableAccount(Long id);

    /**
     * 解禁管理员账号
     *
     * @param id 管理员ID
     */
    void enableAccount(Long id);

    /**
     * 批量禁用管理员账号
     *
     * @param ids 管理员ID列表
     */
    void batchDisableAccount(List<Long> ids);

    /**
     * 批量解禁管理员账号
     *
     * @param ids 管理员ID列表
     */
    void batchEnableAccount(List<Long> ids);

}
