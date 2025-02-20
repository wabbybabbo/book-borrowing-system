package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.SystemNotice;
import org.example.admin.pojo.dto.BatchDTO;
import org.example.admin.pojo.dto.CreateSystemNoticeDTO;
import org.example.admin.pojo.dto.UpdateSystemNoticeDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.service.ISystemNoticeService;
import org.example.common.constant.MessageConstant;
import org.example.common.result.PageResult;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统通知消息表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-16
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system-notice")
@Tag(name = "系统通知消息相关接口")
public class SystemNoticeController {

    private final ISystemNoticeService systemNoticeService;

    @GetMapping("/page")
    @Operation(summary = "分页查询系统通知消息")
    public Result<PageResult<SystemNotice>> pageQuery(@ParameterObject PageQuery pageQuery) {
        log.info("[log] 分页查询系统通知消息 {}", pageQuery);
        PageResult<SystemNotice> pageResult = systemNoticeService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

    @PostMapping
    @Operation(summary = "新建系统通知消息")
    public Result<Object> createSystemNotice(@RequestBody @Valid CreateSystemNoticeDTO createSystemNoticeDTO) {
        log.info("[log] 新建系统通知消息 {}", createSystemNoticeDTO);
        systemNoticeService.createSystemNotice(createSystemNoticeDTO);
        return Result.success(MessageConstant.CREATE_SUCCESS);
    }

    @PutMapping
    @Operation(summary = "更改系统通知消息")
    public Result<Object> updateSystemNotice(@RequestBody @Valid UpdateSystemNoticeDTO updateSystemNoticeDTO) {
        log.info("[log] 更改系统通知消息 {}", updateSystemNoticeDTO);
        systemNoticeService.updateSystemNotice(updateSystemNoticeDTO);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    @DeleteMapping
    @Operation(summary = "删除系统通知消息")
    public Result<Object> deleteSystemNotice(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "出版社ID", required = true)
            String id) {
        log.info("[log] 删除系统通知消息 id: {}", id);
        systemNoticeService.deleteSystemNotice(id);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除系统通知消息")
    public Result<Object> batchDeleteSystemNotices(
            @RequestBody @Valid
            BatchDTO batchDTO) {
        log.info("[log] 批量删除系统通知消息 {}", batchDTO);
        systemNoticeService.batchDeleteSystemNotices(batchDTO.getIds());
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    @PostMapping("/publish")
    @Operation(summary = "发布系统通知消息给所有用户")
    public Result<Object> publishSystemNotice(
            @RequestParam
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "系统通知消息ID", required = true)
            String id) {
        log.info("[log] 发布系统通知消息给所有用户");
        systemNoticeService.publishSystemNotice(id);
        return Result.success(MessageConstant.SEND_SUCCESS);
    }


}
