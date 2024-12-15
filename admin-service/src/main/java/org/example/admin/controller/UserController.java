package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.User;
import org.example.admin.pojo.dto.CreateUserDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.service.IUserService;
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
 * 用户信息表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "用户相关接口")
public class UserController {

    private final IUserService userService;

    @Operation(summary = "分页查询用户信息")
    //只有当PageQuery对象中的filterConditions和sortBy都为null时才会进行缓存
    @Cacheable(cacheNames = "userCache", key = "'userList'+':'+#pageQuery.current+':'+#pageQuery.size", condition = "#pageQuery.filterConditions.empty && #pageQuery.sortBy.blank")
    @GetMapping("/page")
    public Result<PageResult<User>> pageQuery(@ParameterObject PageQuery pageQuery) {
        log.info("[log] 分页查询用户信息 pageQuery: {}", pageQuery);
        PageResult<User> pageResult = userService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

    @Operation(summary = "新建用户信息")
    @CacheEvict(cacheNames = "userCache", allEntries = true)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE/*指定multipart/form-data*/)
    public Result createUser(
            @Parameter(description = "用户头像图片文件")
            @RequestPart(value = "file", required = false)
            MultipartFile file,
            @RequestPart
            @Valid
            CreateUserDTO createUserDTO) {
        log.info("[log] 新建用户信息 createUserDTO: {}", createUserDTO);
        userService.createUser(file, createUserDTO);
        return Result.success(MessageConstant.CREATE_SUCCESS);
    }

    @Operation(summary = "禁用用户账号")
    @CacheEvict(cacheNames = "userCache", allEntries = true)
    @PutMapping("/disable")
    public Result disableUser(
            @Parameter(description = "用户ID")
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            Long id
    ) {
        log.info("[log] 禁用用户账号 id: {}", id);
        userService.disableAccount(id);
        return Result.success(MessageConstant.DISABLE_SUCCESS);
    }

    @Operation(summary = "批量禁用用户账号")
    @CacheEvict(cacheNames = "userCache", allEntries = true)
    @PutMapping("/disable/batch")
    public Result batchDisableAccount(
            @RequestBody
            @NotEmpty(message = MessageConstant.FIELD_NOT_EMPTY)
            List<Long> ids
    ) {
        log.info("[log] 批量禁用用户账号 ids: {}", ids);
        userService.batchDisableAccount(ids);
        return Result.success(MessageConstant.DISABLE_SUCCESS);
    }

    @Operation(summary = "解禁用户账号")
    @CacheEvict(cacheNames = "userCache", allEntries = true)
    @PutMapping("/enable")
    public Result enableUser(
            @Parameter(description = "用户ID")
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            Long id
    ) {
        log.info("[log] 解禁用户账号 id: {}", id);
        userService.enableAccount(id);
        return Result.success(MessageConstant.ENABLE_SUCCESS);
    }

    @Operation(summary = "批量解禁用户账号")
    @CacheEvict(cacheNames = "userCache", allEntries = true)
    @PutMapping("/enable/batch")
    public Result batchEnableUser(
            @RequestBody
            @NotEmpty(message = MessageConstant.FIELD_NOT_EMPTY)
            List<Long> ids
    ) {
        log.info("[log] 批量解禁用户账号 ids: {}", ids);
        userService.batchEnableAccount(ids);
        return Result.success(MessageConstant.ENABLE_SUCCESS);
    }

}
