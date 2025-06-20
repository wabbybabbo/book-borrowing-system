package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.example.admin.entity.Admin;
import org.example.admin.pojo.dto.AdminLoginDTO;
import org.example.admin.pojo.dto.CreateAdminDTO;
import org.example.admin.pojo.dto.UpdateAdminDTO;
import org.example.admin.pojo.dto.UpdateEmailDTO;
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
     * 创建动态图形验证码，并以时间戳作为key将验证码缓存到redis中
     *
     * @param timestamp 时间戳
     * @param response  HTTP响应
     */
    void createGifCaptcha(String timestamp, HttpServletResponse response);

    /**
     * 管理员登录
     *
     * @param adminLoginDTO {@link AdminLoginDTO}
     * @return {@link AdminLoginVO}
     */
    AdminLoginVO login(AdminLoginDTO adminLoginDTO);

    /**
     * 分页查询管理员信息
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageResult<Admin>}
     */
    PageResult<Admin> pageQuery(PageQuery pageQuery);

    /**
     * 新建管理员信息
     *
     * @param file           管理员头像图片文件
     * @param createAdminDTO {@link CreateAdminDTO}
     */
    void createAdmin(MultipartFile file, CreateAdminDTO createAdminDTO);

    /**
     * 更改管理员信息
     *
     * @param id             管理员ID
     * @param updateAdminDTO {@link UpdateAdminDTO}
     */
    void updateAdmin(String id, UpdateAdminDTO updateAdminDTO);

    /**
     * 更换管理员头像
     *
     * @param id   管理员ID
     * @param file 头像图片文件
     * @return 头像图片访问地址
     */
    String updateAvatar(String id, MultipartFile file);

    /**
     * 发送验证码到邮箱用于换绑邮箱
     *
     * @param email   用户邮箱
     * @param timeout 验证码有效时长（分钟）
     */
    void sendCaptcha2Email4UpdateEmail(String email, Long timeout);

    /**
     * 换绑邮箱
     *
     * @param id             用户ID
     * @param updateEmailDTO {@link UpdateEmailDTO}
     */
    void updateEmail(String id, UpdateEmailDTO updateEmailDTO);

    /**
     * 禁用管理员账号
     *
     * @param id 管理员ID
     */
    void disableAccount(String id);

    /**
     * 解禁管理员账号
     *
     * @param id 管理员ID
     */
    void enableAccount(String id);

    /**
     * 批量禁用管理员账号
     *
     * @param ids 管理员ID列表
     */
    void batchDisableAccounts(List<String> ids);

    /**
     * 批量解禁管理员账号
     *
     * @param ids 管理员ID列表
     */
    void batchEnableAccounts(List<String> ids);

}
