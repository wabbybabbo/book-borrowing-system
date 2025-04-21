package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.admin.pojo.dto.SendReminderDTO;
import org.example.admin.service.IReminderService;
import org.example.common.constant.MessageConstant;
import org.example.common.result.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户提醒消息表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/reminder")
@Tag(name = "用户提醒消息相关接口")
public class ReminderController {

    private final IReminderService reminderService;

    @PostMapping("/send")
    @Operation(summary = "手动发送提醒消息给用户")
    public Result<Object> sendReminder(@RequestBody @Valid SendReminderDTO sendReminderDTO) {
        reminderService.sendReminder(sendReminderDTO);
        return Result.success(MessageConstant.SEND_SUCCESS);
    }

    @PostMapping("/task/send-reminders")
    @Operation(summary = "触发定时任务-根据借阅记录状态和时间（预约日期或预计归还日期）发送相应的提醒信息给用户")
    public void sendReminders() {
        reminderService.sendReservedReminders(1);
        reminderService.sendBorrowingReminders(1);
        reminderService.sendReserveOverdueReminders();
        reminderService.sendReturnOverdueReminders();
    }

}
