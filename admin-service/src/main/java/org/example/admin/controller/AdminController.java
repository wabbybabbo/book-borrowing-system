package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.Admin;
import org.example.admin.pojo.dto.*;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.AdminLoginVO;
import org.example.admin.service.IAdminService;
import org.example.common.constant.ClaimConstant;
import org.example.common.constant.MessageConstant;
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
@Slf4j
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
        log.info("[log] 开始获取动态图形验证码");
        adminService.createGifCaptcha(timestamp, response);
    }

    @GetMapping("/login")
    @Operation(summary = "管理员登录")
    public Result<AdminLoginVO> login(@ParameterObject @Valid AdminLoginDTO adminLoginDTO) {
        log.info("[log] 管理员登录 {}", adminLoginDTO);
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
        log.info("[log] 分页查询管理员信息 {}", pageQuery);
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
        log.info("[log] 新建管理员信息 {}", createAdminDTO);
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
        log.info("[log] 更改管理员信息 id: {}, {}", id, updateAdminDTO);
        adminService.updateAdmin(id, updateAdminDTO);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    @PutMapping("/disable")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @Operation(summary = "禁用管理员账号")
    public Result<Object> disableAccount(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "管理员ID", required = true)
            String id) {
        log.info("[log] 禁用管理员账号 id: {}", id);
        adminService.disableAccount(id);
        return Result.success(MessageConstant.DISABLE_ACCOUNT_SUCCESS);
    }

    @PutMapping("/disable/batch")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @Operation(summary = "批量禁用管理员账号")
    public Result<Object> batchDisableAccounts(
            @RequestBody @Valid
            BatchDisableAccountsDTO batchDisableAccountsDTO) {
        log.info("[log] 批量禁用管理员账号 {}", batchDisableAccountsDTO);
        adminService.batchDisableAccounts(batchDisableAccountsDTO.getIds());
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
        log.info("[log] 解禁管理员账号 id: {}", id);
        adminService.enableAccount(id);
        return Result.success(MessageConstant.ENABLE_ACCOUNT_SUCCESS);
    }

    @PutMapping("/enable/batch")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @Operation(summary = "批量解禁管理员账号")
    public Result<Object> batchEnableAccounts(
            @RequestBody @Valid
            BatchEnableAccountsDTO batchEnableAccountsDTO) {
        log.info("[log] 批量解禁管理员账号 {}", batchEnableAccountsDTO);
        adminService.batchEnableAccounts(batchEnableAccountsDTO.getIds());
        return Result.success(MessageConstant.ENABLE_ACCOUNT_SUCCESS);
    }

}
