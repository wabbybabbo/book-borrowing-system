package org.example.client.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.entity.Reminder;
import org.example.client.mapper.ReminderMapper;
import org.example.client.service.IReminderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户提醒消息表 服务实现类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ReminderServiceImpl extends ServiceImpl<ReminderMapper, Reminder> implements IReminderService {

}
