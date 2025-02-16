package org.example.admin.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.service.IBorrowService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务类，定时处理借阅记录
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BorrowTask {

    private final IBorrowService borrowService;

    /**
     * 定时更新借阅记录的状态
     */
    @Scheduled(cron = "0 0 0 * * ?") //每天凌晨0点触发执行
    public void updateBorrows() {
        log.info("[log] 定时更新借阅记录的状态");
        borrowService.updateBorrowingBorrows();
        borrowService.updateReservedBorrows();
    }

}

