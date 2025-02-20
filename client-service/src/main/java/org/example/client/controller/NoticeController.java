package org.example.client.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.pojo.dto.BatchDTO;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.NoticeVO;
import org.example.client.service.INoticeService;
import org.example.common.constant.ClaimConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.result.PageResult;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户的通知消息表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-16
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/notice")
@Tag(name = "用户的通知消息相关接口")
public class NoticeController {

    private final INoticeService userNoticeService;

    @GetMapping("/page")
    @Operation(summary = "分页查询用户的通知消息")
    public Result<PageResult<NoticeVO>> pageQuery(
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "用户ID", required = true, hidden = true)
            String id,
            @ParameterObject
            PageQuery pageQuery) {
        log.info("[log] 分页查询用户的通知消息 id: {}, {}", id, pageQuery);
        PageResult<NoticeVO> pageResult = userNoticeService.pageQuery(id, pageQuery);
        return Result.success(pageResult);
    }

    @PostMapping
    @Operation(summary = "新增用户的通知消息")
    public Result<Object> createUserNotice(
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "用户ID", required = true, hidden = true)
            String userId,
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "系统通知消息ID", required = true)
            String systemNoticeId) {
        log.info("[log] 新增用户的通知消息 userId: {}, systemNoticeId: {}", userId, systemNoticeId);
        userNoticeService.createNotice(userId, systemNoticeId);
        return Result.success();
    }

    @GetMapping("/unread/count")
    @Operation(summary = "获取未读通知消息的数量")
    public Result<Long> getUnreadUserNoticeCount() {
        log.info("[log] 获取未读通知消息的数量");
        Long count = userNoticeService.getUnreadNoticeCount();
        return Result.success(count);
    }

    @PutMapping("/read")
    @Operation(summary = "将用户的通知消息标为已读")
    public Result<Object> readUserNotice(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "通知消息ID", required = true)
            String id) {
        log.info("[log] 将用户的通知消息标为已读 id: {}", id);
        userNoticeService.readNotice(id);
        return Result.success();
    }

    @PutMapping("/batch/read")
    @Operation(summary = "批量将用户的通知消息标为已读")
    public Result<Object> batchReadUserNotices(
            @RequestBody @Valid
            BatchDTO batchDTO) {
        log.info("[log] 批量将用户的通知消息标为已读 {}", batchDTO);
        userNoticeService.batchReadNotices(batchDTO.getIds());
        return Result.success();
    }

    @PutMapping("/unread")
    @Operation(summary = "将用户的通知消息标为未读")
    public Result<Object> unreadUserNotice(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "通知消息ID", required = true)
            String id) {
        log.info("[log] 将用户的通知消息标为未读 id: {}", id);
        userNoticeService.unreadNotice(id);
        return Result.success();
    }

    @PutMapping("/batch/unread")
    @Operation(summary = "批量将用户的通知消息标为未读")
    public Result<Object> batchUnreadUserNotices(
            @RequestBody @Valid
            BatchDTO batchDTO) {
        log.info("[log] 批量将用户的通知消息标为未读 {}", batchDTO);
        userNoticeService.batchUnreadNotices(batchDTO.getIds());
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "删除用户的通知消息")
    public Result<Object> deleteUserNotice(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "通知消息ID", required = true)
            String id) {
        log.info("[log] 删除用户的通知消息 id: {}", id);
        userNoticeService.deleteNotice(id);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户的通知消息")
    public Result<Object> batchDeleteUserNotices(
            @RequestBody @Valid
            BatchDTO batchDTO) {
        log.info("[log] 批量删除用户的通知消息 {}", batchDTO);
        userNoticeService.batchDeleteNotices(batchDTO.getIds());
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

}
