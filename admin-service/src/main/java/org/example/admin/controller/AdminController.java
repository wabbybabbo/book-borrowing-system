package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    @Operation(summary = "获取动态图形验证码")
    @GetMapping(value = "/captcha")
    public void getGifCaptcha(
            @Parameter(description = "时间戳", required = true)
            String timestamp,
            HttpServletResponse response
    ) {
        log.info("[log] 开始获取动态图形验证码");
        String code = adminService.createGifCaptcha(timestamp, response);
        log.info("[log] 生成的验证码为：{}", code);
    }

    @Operation(summary = "管理员登录")
    @GetMapping("/login")
    public Result<AdminLoginVO> login(@ParameterObject @Valid AdminLoginDTO adminLoginDTO) {
        log.info("[log] 管理员登录 {}", adminLoginDTO);
        String code = adminService.getCodeCache(adminLoginDTO.getTimestamp());
        AdminLoginVO adminLoginVO = adminService.login(adminLoginDTO, code);
        return Result.success(MessageConstant.LOGIN_SUCCESS, adminLoginVO);
    }

    @Operation(summary = "分页查询管理员信息")
    //只有当PageQuery对象中的filterConditions和sortBy都为null时才会进行缓存
    @Cacheable(cacheNames = "adminCache", key = "'adminList'+':'+#pageQuery.current+':'+#pageQuery.size", condition = "#pageQuery.filterConditions.empty && #pageQuery.sortBy.blank")
    @GetMapping("/page")
    public Result<PageResult<Admin>> pageQuery(@ParameterObject PageQuery pageQuery) {
        log.info("[log] 分页查询管理员信息 {}", pageQuery);
        PageResult<Admin> pageResult = adminService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

    @Operation(summary = "新建管理员信息")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE/*指定multipart/form-data*/)
    public Result<Object> createAdmin(
            @Parameter(description = "管理员头像图片文件")
            @RequestPart(value = "file", required = false)
            MultipartFile file,
            @RequestPart @Valid
            CreateAdminDTO createAdminDTO
    ) {
        log.info("[log] 新建管理员信息 {}", createAdminDTO);
        adminService.createAdmin(file, createAdminDTO);
        return Result.success(MessageConstant.CREATE_SUCCESS);
    }

    @Operation(summary = "更改管理员信息")
    @PutMapping
    public Result<Object> updateAdmin(
            @RequestBody @Valid
            UpdateAdminDTO updateAdminDTO,
            @Parameter(description = "管理员ID", hidden = true)
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            String id
    ) {
        log.info("[log] 更改管理员信息 {}, id: {}", updateAdminDTO, id);
        adminService.updateAdmin(updateAdminDTO, id);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    @Operation(summary = "禁用管理员账号")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @PutMapping("/disable")
    public Result<Object> disableAccount(
            @Parameter(description = "管理员ID", required = true)
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            String id
    ) {
        log.info("[log] 禁用管理员账号 id: {}", id);
        adminService.disableAccount(id);
        return Result.success(MessageConstant.DISABLE_SUCCESS);
    }

    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @PutMapping("/disable/batch")
    @Operation(summary = "批量禁用管理员账号")
    public Result<Object> batchDisableAccount(
            @RequestBody @Valid
            BatchDisableAccountDTO batchDisableAccountDTO
    ) {
        log.info("[log] 批量禁用管理员账号 {}", batchDisableAccountDTO);
        adminService.batchDisableAccount(batchDisableAccountDTO.getIds());
        return Result.success(MessageConstant.DISABLE_SUCCESS);
    }

    @Operation(summary = "解禁管理员账号")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @PutMapping("/enable")
    public Result<Object> enableAccount(
            @Parameter(description = "管理员ID", required = true)
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            String id
    ) {
        log.info("[log] 解禁管理员账号 id: {}", id);
        adminService.enableAccount(id);
        return Result.success(MessageConstant.ENABLE_SUCCESS);
    }

    @Operation(summary = "批量解禁管理员账号")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @PutMapping("/enable/batch")
    public Result<Object> batchEnableAccount(
            @RequestBody @Valid
            BatchEnableAccountDTO batchEnableAccountDTO
    ) {
        log.info("[log] 批量解禁管理员账号 {}", batchEnableAccountDTO);
        adminService.batchEnableAccount(batchEnableAccountDTO.getIds());
        return Result.success(MessageConstant.ENABLE_SUCCESS);
    }

}
