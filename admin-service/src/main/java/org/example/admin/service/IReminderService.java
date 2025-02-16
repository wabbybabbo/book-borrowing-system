package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.entity.Reminder;
import org.example.admin.pojo.dto.SendReminderDTO;

/**
 * <p>
 * 用户提醒消息表 服务类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
public interface IReminderService extends IService<Reminder> {

    /**
     * 根据该借阅记录状态发送相应的提醒信息给用户
     *
     * @param sendReminderDTO {@link SendReminderDTO}
     */
    void sendReminder(SendReminderDTO sendReminderDTO);

    /**
     * 向用户发送借阅预约提醒消息
     *
     * @param days 向距离预约日期<=days天的用户发送消息
     */
    void sendReservedReminders(long days);

    /**
     * 向用户发送借阅归还提醒消息
     *
     * @param days 向距离预计归还日期<=days天的用户发送消息
     */
    void sendBorrowingReminders(long days);

    /**
     * 向用户发送借阅预约逾期提醒消息
     */
    void sendReserveOverdueReminders();

    /**
     * 向用户发送借阅归还逾期提醒消息
     */
    void sendReturnOverdueReminders();

}
