package org.example.admin.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.service.IReminderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务类，定时处理提醒消息
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ReminderTask {

    private final IReminderService reminderService;

    /**
     * 定时根据借阅记录状态和时间（预约日期或预计归还日期）发送相应的提醒信息给用户
     */
    @Scheduled(cron = "0 0 8 * * ?") //每天早上8点触发执行
    public void sendReminders() {
        log.info("[log] 定时根据借阅记录状态和时间（预约日期或预计归还日期）发送相应的提醒信息给用户");
        reminderService.sendReservedReminders(1);
        reminderService.sendBorrowingReminders(1);
        reminderService.sendReserveOverdueReminders();
        reminderService.sendReturnOverdueReminders();
    }

}

