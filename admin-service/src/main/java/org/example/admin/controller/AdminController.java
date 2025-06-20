package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.example.admin.entity.Admin;
import org.example.admin.pojo.dto.*;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.AdminLoginVO;
import org.example.admin.service.IAdminService;
import org.example.common.constant.ClaimConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;
import org.example.common.result.PageResult;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 管理员信息表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-12-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@Tag(name = "管理员相关接口")
public class AdminController {

    private final IAdminService adminService;

    @GetMapping(value = "/login/captcha")
    @Operation(summary = "获取动态图形验证码")
    public void getGifCaptcha(
            @Parameter(description = "时间戳", required = true)
            String timestamp,
            HttpServletResponse response) {
        adminService.createGifCaptcha(timestamp, response);
    }

    @GetMapping("/login")
    @Operation(summary = "管理员登录")
    public Result<AdminLoginVO> login(@ParameterObject @Valid AdminLoginDTO adminLoginDTO) {
        AdminLoginVO adminLoginVO = adminService.login(adminLoginDTO);
        return Result.success(MessageConstant.LOGIN_SUCCESS, adminLoginVO);
    }

    @GetMapping("/page")
    @Cacheable(cacheNames = "adminCache",
            key = "'admin-service' + ':' + 'adminList' + ':' + #pageQuery.current + ':' + #pageQuery.size",
            /*只有当PageQuery对象中的filterConditions和sortBy都为null时才会进行缓存*/
            condition = "#pageQuery.filterConditions.empty && #pageQuery.sortBy.blank")
    @Operation(summary = "分页查询管理员信息")
    public Result<PageResult<Admin>> pageQuery(@ParameterObject PageQuery pageQuery) {
        PageResult<Admin> pageResult = adminService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE /*指定multipart/form-data*/)
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @Operation(summary = "新建管理员信息")
    public Result<Object> createAdmin(
            @RequestPart(value = "file", required = false)
            @Parameter(description = "管理员头像图片文件")
            MultipartFile file,
            @RequestPart @Valid
            CreateAdminDTO createAdminDTO) {
        adminService.createAdmin(file, createAdminDTO);
        return Result.success(MessageConstant.CREATE_SUCCESS);
    }

    @PutMapping
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @Operation(summary = "更改管理员信息")
    public Result<Object> updateAdmin(
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "管理员ID", required = true, hidden = true)
            String id,
            @RequestBody @Valid
            UpdateAdminDTO updateAdminDTO) {
        adminService.updateAdmin(id, updateAdminDTO);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE /*指定multipart/form-data*/)
    @Operation(summary = "更换管理员头像")
    public Result<String> updateAvatar(
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "管理员ID", required = true, hidden = true)
            String id,
            @RequestPart("file")
            @Parameter(description = "管理员头像图片文件")
            MultipartFile file) {
        String url = adminService.updateAvatar(id, file);
        return Result.success(MessageConstant.UPDATE_SUCCESS, url);
    }

    @GetMapping(value = "/email/captcha")
    @Operation(summary = "发送验证码到邮箱用于换绑邮箱，并设置验证码有效时长")
    public Result<String> sendCaptcha2Email4UpdateEmail(
            @Email(regexp = RegexpConstant.EMAIL, message = MessageConstant.INVALID_EMAIL)
            @Parameter(description = "邮箱", required = true)
            String email,
            @PositiveOrZero(message = MessageConstant.INVALID_CAPTCHA_TIMEOUT)
            @Parameter(description = "验证码有效时长（分钟）", required = true)
            Long timeout) {
        adminService.sendCaptcha2Email4UpdateEmail(email, timeout);
        return Result.success(MessageConstant.SEND_CAPTCHA_SUCCESS);
    }

    @PutMapping("/email")
    @Operation(summary = "换绑邮箱")
    public Result<Object> updateEmail(
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "用户ID", required = true, hidden = true)
            String id,
            @RequestBody @Valid
            UpdateEmailDTO updateEmailDTO) {
        adminService.updateEmail(id, updateEmailDTO);
        return Result.success(MessageConstant.UPDATE_EMAIL_SUCCESS);
    }

    @PutMapping("/disable")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @Operation(summary = "禁用管理员账号")
    public Result<Object> disableAccount(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "管理员ID", required = true)
            String id) {
        adminService.disableAccount(id);
        return Result.success(MessageConstant.DISABLE_ACCOUNT_SUCCESS);
    }

    @PutMapping("/disable/batch")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @Operation(summary = "批量禁用管理员账号")
    public Result<Object> batchDisableAccounts(
            @RequestBody @Valid
            BatchDTO batchDTO) {
        adminService.batchDisableAccounts(batchDTO.getIds());
        return Result.success(MessageConstant.DISABLE_ACCOUNT_SUCCESS);
    }

    @PutMapping("/enable")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @Operation(summary = "解禁管理员账号")
    public Result<Object> enableAccount(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "管理员ID", required = true)
            String id) {
        adminService.enableAccount(id);
        return Result.success(MessageConstant.ENABLE_ACCOUNT_SUCCESS);
    }

    @PutMapping("/enable/batch")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @Operation(summary = "批量解禁管理员账号")
    public Result<Object> batchEnableAccounts(
            @RequestBody @Valid
            BatchDTO batchDTO) {
        adminService.batchEnableAccounts(batchDTO.getIds());
        return Result.success(MessageConstant.ENABLE_ACCOUNT_SUCCESS);
    }

}
