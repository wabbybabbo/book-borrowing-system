package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.Admin;
import org.example.admin.pojo.dto.AdminLoginDTO;
import org.example.admin.pojo.dto.CreateAdminDTO;
import org.example.admin.pojo.dto.UpdateAdminDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.AdminLoginVO;
import org.example.admin.service.IAdminService;
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

import java.util.List;

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

    @Operation(summary = "管理员登录")
    @GetMapping("/login")
    public Result<AdminLoginVO> login(@ParameterObject @Valid AdminLoginDTO adminLoginDTO) {
        log.info("[log] 管理员登录 {}", adminLoginDTO);

        //查询登录的管理员信息
        AdminLoginVO adminLoginVO = adminService.login(adminLoginDTO);

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
    public Result createAccount(
            @Parameter(description = "管理员头像图片文件")
            @RequestPart(value = "file", required = false)
            MultipartFile file,
            @RequestPart
            @Valid
            CreateAdminDTO createAdminDTO) {
        log.info("[log] 新建管理员信息 {}", createAdminDTO);
        adminService.createAdmin(file, createAdminDTO);
        return Result.success(MessageConstant.CREATE_SUCCESS);
    }

    @Operation(summary = "更改管理员信息")
    @PutMapping
    public Result updateAdmin(@RequestBody @Valid UpdateAdminDTO updateAdminDTO) {
        log.info("[log] 更改管理员信息 {}", updateAdminDTO);
        adminService.updateAdmin(updateAdminDTO);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    @Operation(summary = "禁用管理员账号")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @PutMapping("/disable")
    public Result disableAccount(
            @Parameter(description = "管理员ID")
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            Long id
    ) {
        log.info("[log] 禁用管理员账号 id: {}", id);
        adminService.disableAccount(id);
        return Result.success(MessageConstant.DISABLE_SUCCESS);
    }

    @Operation(summary = "批量禁用管理员账号")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @PutMapping("/disable/batch")
    public Result batchDisableAccount(
            @RequestBody
            @NotEmpty(message = MessageConstant.FIELD_NOT_EMPTY)
            List<Long> ids
    ) {
        log.info("[log] 批量禁用管理员账号 ids: {}", ids);
        adminService.batchDisableAccount(ids);
        return Result.success(MessageConstant.DISABLE_SUCCESS);
    }

    @Operation(summary = "解禁管理员账号")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @PutMapping("/enable")
    public Result enableAccount(
            @Parameter(description = "管理员ID")
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            Long id
    ) {
        log.info("[log] 解禁管理员账号 id: {}", id);
        adminService.enableAccount(id);
        return Result.success(MessageConstant.ENABLE_SUCCESS);
    }

    @Operation(summary = "批量解禁管理员账号")
    @CacheEvict(cacheNames = "adminCache", allEntries = true)
    @PutMapping("/enable/batch")
    public Result batchEnableAccount(
            @RequestBody
            @NotEmpty(message = MessageConstant.FIELD_NOT_EMPTY)
            List<Long> ids
    ) {
        log.info("[log] 批量解禁管理员账号 ids: {}", ids);
        adminService.batchEnableAccount(ids);
        return Result.success(MessageConstant.ENABLE_SUCCESS);
    }

}
