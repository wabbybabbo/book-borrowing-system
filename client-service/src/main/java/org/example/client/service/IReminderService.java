package org.example.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.client.entity.Reminder;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.ReminderVO;
import org.example.common.result.PageResult;

import java.util.List;

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
     * 分页查询用户的提醒消息
     *
     * @param id        用户ID
     * @param pageQuery {@link PageQuery}
     * @return 用户提醒消息列表
     */
    PageResult<ReminderVO> pageQuery(String id, PageQuery pageQuery);

    /**
     * 获取未读提醒消息的数量
     *
     * @param id        用户ID
     * @return 未读提醒消息的数量
     */
    Long getUnreadReminderCount(String id);

    /**
     * 将用户的提醒消息标为已读
     *
     * @param id 提醒消息ID
     */
    void readReminder(String id);

    /**
     * 批量将用户的提醒消息标为已读
     *
     * @param ids 提醒消息ID列表
     */
    void batchReadReminders(List<String> ids);

    /**
     * 将用户的提醒消息标为未读
     *
     * @param id 提醒消息ID
     */
    void unreadReminder(String id);

    /**
     * 批量将用户的提醒消息标为未读
     *
     * @param ids 提醒消息ID列表
     */
    void batchUnreadReminders(List<String> ids);

    /**
     * 删除用户的提醒消息
     *
     * @param id 提醒消息ID
     */
    void deleteReminder(String id);

    /**
     * 批量删除用户的提醒消息
     *
     * @param ids 提醒消息ID列表
     */
    void batchDeleteReminders(List<String> ids);

}
