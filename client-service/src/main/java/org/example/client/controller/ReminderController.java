package org.example.client.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.client.pojo.dto.BatchDTO;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.ReminderVO;
import org.example.client.service.IReminderService;
import org.example.common.constant.ClaimConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.result.PageResult;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户的提醒消息表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/reminder")
@Tag(name = "用户的提醒消息相关接口")
public class ReminderController {

    private final IReminderService reminderService;

    @GetMapping("/page")
    @Operation(summary = "分页查询用户的提醒消息")
    public Result<PageResult<ReminderVO>> pageQuery(
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "用户ID", required = true, hidden = true)
            String id,
            @ParameterObject
            PageQuery pageQuery) {
        PageResult<ReminderVO> pageResult = reminderService.pageQuery(id, pageQuery);
        return Result.success(pageResult);
    }

    @GetMapping("/unread/count")
    @Operation(summary = "获取未读提醒消息的数量")
    public Result<Long> getUnreadReminderCount() {
        Long count = reminderService.getUnreadReminderCount();
        return Result.success(count);
    }

    @PutMapping("/read")
    @Operation(summary = "将用户的提醒消息标为已读")
    public Result<Object> readReminder(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "提醒消息ID", required = true)
            String id) {
        reminderService.readReminder(id);
        return Result.success();
    }

    @PutMapping("/batch/read")
    @Operation(summary = "批量将用户的提醒消息标为已读")
    public Result<Object> batchReadReminders(
            @RequestBody @Valid
            BatchDTO batchDTO) {
        reminderService.batchReadReminders(batchDTO.getIds());
        return Result.success();
    }

    @PutMapping("/unread")
    @Operation(summary = "将用户的提醒消息标为未读")
    public Result<Object> unreadReminder(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "提醒消息ID", required = true)
            String id) {
        reminderService.unreadReminder(id);
        return Result.success();
    }

    @PutMapping("/batch/unread")
    @Operation(summary = "批量将用户的提醒消息标为未读")
    public Result<Object> batchUnreadReminders(
            @RequestBody @Valid
            BatchDTO batchDTO) {
        reminderService.batchUnreadReminders(batchDTO.getIds());
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "删除用户的提醒消息")
    public Result<Object> deleteReminder(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "提醒消息ID", required = true)
            String id) {
        reminderService.deleteReminder(id);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户的提醒消息")
    public Result<Object> batchDeleteReminders(
            @RequestBody @Valid
            BatchDTO batchDTO) {
        reminderService.batchDeleteReminders(batchDTO.getIds());
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

}
