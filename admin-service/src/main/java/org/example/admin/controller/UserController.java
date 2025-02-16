package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.entity.User;
import org.example.admin.pojo.dto.BatchDisableAccountsDTO;
import org.example.admin.pojo.dto.BatchEnableAccountsDTO;
import org.example.admin.pojo.dto.CreateUserDTO;
import org.example.admin.service.IUserService;
import org.example.common.constant.MessageConstant;
import org.example.common.result.PageResult;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/page")
    @Operation(summary = "分页查询用户信息")
    public Result<PageResult<User>> pageQuery(@ParameterObject PageQuery pageQuery) {
        log.info("[log] 分页查询用户信息 {}", pageQuery);
        PageResult<User> pageResult = userService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE/*指定multipart/form-data*/)
    @Operation(summary = "新建用户信息")
    public Result<Object> createUser(
            @RequestPart(value = "file", required = false)
            @Parameter(description = "用户头像图片文件")
            MultipartFile file,
            @RequestPart @Valid
            CreateUserDTO createUserDTO) {
        log.info("[log] 新建用户信息 {}", createUserDTO);
        userService.createUser(file, createUserDTO);
        return Result.success(MessageConstant.CREATE_SUCCESS);
    }

    @PutMapping("/disable")
    @Operation(summary = "禁用用户账号")
    public Result<Object> disableAccount(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "用户ID", required = true)
            String id) {
        log.info("[log] 禁用用户账号 id: {}", id);
        userService.disableAccount(id);
        return Result.success(MessageConstant.DISABLE_ACCOUNT_SUCCESS);
    }

    @PutMapping("/disable/batch")
    @Operation(summary = "批量禁用用户账号")
    public Result<Object> batchDisableAccounts(
            @RequestBody @Valid
            BatchDisableAccountsDTO batchDisableAccountsDTO) {
        log.info("[log] 批量禁用用户账号 {}", batchDisableAccountsDTO);
        userService.batchDisableAccounts(batchDisableAccountsDTO.getIds());
        return Result.success(MessageConstant.DISABLE_ACCOUNT_SUCCESS);
    }

    @PutMapping("/enable")
    @Operation(summary = "解禁用户账号")
    public Result<Object> enableAccount(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "用户ID", required = true)
            String id) {
        log.info("[log] 解禁用户账号 id: {}", id);
        userService.enableAccount(id);
        return Result.success(MessageConstant.ENABLE_ACCOUNT_SUCCESS);
    }

    @PutMapping("/enable/batch")
    @Operation(summary = "批量解禁用户账号")
    public Result<Object> batchEnableAccounts(
            @RequestBody @Valid
            BatchEnableAccountsDTO batchEnableAccountsDTO) {
        log.info("[log] 批量解禁用户账号 {}", batchEnableAccountsDTO);
        userService.batchEnableAccounts(batchEnableAccountsDTO.getIds());
        return Result.success(MessageConstant.ENABLE_ACCOUNT_SUCCESS);
    }

}
