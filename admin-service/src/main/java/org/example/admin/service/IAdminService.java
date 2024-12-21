package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
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
     * 管理员登录
     *
     * @param adminLoginDTO 管理员登录时传递的数据模型
     * @param code          redis缓存中的验证码
     * @return 登录的管理员信息
     */
    AdminLoginVO login(AdminLoginDTO adminLoginDTO, String code);

    /**
     * 分页查询管理员信息
     *
     * @param pageQuery 分页查询时传递的数据模型
     * @return 管理员信息列表
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
     * @param id             管理员ID
     */
    void updateAdmin(UpdateAdminDTO updateAdminDTO, String id);

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
