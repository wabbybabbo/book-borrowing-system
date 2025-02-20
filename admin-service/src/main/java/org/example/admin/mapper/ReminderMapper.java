package org.example.admin.mapper;

import org.example.admin.entity.Reminder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户提醒消息表 Mapper 接口
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
public interface ReminderMapper extends BaseMapper<Reminder> {

    /**
     * 新增用户提醒消息
     *
     * @param reminder {@link Reminder}
     * @return 用户提醒消息ID
     */
    String insertReminder(Reminder reminder);

}
