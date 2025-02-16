package org.example.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.entity.Reminder;
import org.example.client.mapper.ReminderMapper;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.ReminderVO;
import org.example.client.service.IReminderService;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.NotFoundException;
import org.example.common.result.PageResult;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    private final ReminderMapper reminderMapper;

    @Override
    public PageResult<ReminderVO> pageQuery(String id, PageQuery pageQuery) {
        // 构建分页查询条件
        Page<Reminder> page = pageQuery.toMpPage();
        QueryWrapper<Reminder> queryWrapper = new QueryWrapper<>();
        List<String> filterConditions = pageQuery.getFilterConditions();
        log.info("[log] 用户提醒消息分页查询条件 filterConditions: {}", filterConditions);
        if (CollUtil.isNotEmpty(filterConditions)) {
            for (String condition : filterConditions) {
                if (condition.contains("=")) {
                    log.info("[log] = condition: {}", condition);
                    String[] pair = condition.split("=");
                    if (pair.length == 2)
                        queryWrapper.eq(pair[0], pair[1]);
                } else if (condition.contains("~")) {
                    log.info("[log] ~ condition: {}", condition);
                    String[] pair = condition.split("~");
                    if (pair.length == 2)
                        queryWrapper.like(pair[0], pair[1]);
                }
            }
        }
        LambdaQueryWrapper<Reminder> lambdaQueryWrapper = queryWrapper.lambda()
                .select(Reminder::getId, Reminder::getTitle, Reminder::getContent, Reminder::getIsRead, Reminder::getCreateTime)
                .eq(Reminder::getUserId, id);
        // 分页查询
        try {
            reminderMapper.selectPage(page, lambdaQueryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 用户提醒消息分页查询失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.FIELD_NOT_FOUND);
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }
        List<Reminder> records = page.getRecords();

        //转化为VO
        List<ReminderVO> reminderVOList = records.stream().map(reminder -> {
            ReminderVO reminderVO = new ReminderVO();
            BeanUtil.copyProperties(reminder, reminderVO);
            return reminderVO;
        }).collect(Collectors.toList());

        return PageResult.<ReminderVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(reminderVOList)
                .build();
    }

    @Override
    public Long getUnreadReminderCount() {
        LambdaQueryWrapper<Reminder> queryWrapper = new LambdaQueryWrapper<Reminder>()
                .eq(Reminder::getIsRead, false);
        return reminderMapper.selectCount(queryWrapper);
    }

    @Override
    public void readReminder(String id) {
        Reminder reminder = new Reminder();
        reminder.setId(id);
        reminder.setIsRead(true);
        if (reminderMapper.updateById(reminder) == 0) {
            log.error("[log] 将用户的提醒消息标为已读失败 msg: {}", MessageConstant.RMINDER_NOT_FOUND);
            throw new NotFoundException(MessageConstant.RMINDER_NOT_FOUND);
        }
    }

    @Override
    public void batchReadReminders(List<String> ids) {
        LambdaUpdateWrapper<Reminder> updateWrapper = new LambdaUpdateWrapper<Reminder>()
                .set(Reminder::getIsRead, true)
                .in(Reminder::getId, ids);
        int updates = reminderMapper.update(updateWrapper);
        if (updates == 0) {
            log.error("[log] 批量将用户的提醒消息标为已读失败 msg: {}", MessageConstant.RMINDER_NOT_FOUND);
            throw new NotFoundException(MessageConstant.RMINDER_NOT_FOUND);
        }
    }

    @Override
    public void unreadReminder(String id) {
        Reminder reminder = new Reminder();
        reminder.setId(id);
        reminder.setIsRead(false);
        if (reminderMapper.updateById(reminder) == 0) {
            log.error("[log] 将用户的提醒消息标为未读失败 msg: {}", MessageConstant.RMINDER_NOT_FOUND);
            throw new NotFoundException(MessageConstant.RMINDER_NOT_FOUND);
        }
    }

    @Override
    public void batchUnreadReminders(List<String> ids) {
        LambdaUpdateWrapper<Reminder> updateWrapper = new LambdaUpdateWrapper<Reminder>()
                .set(Reminder::getIsRead, false)
                .in(Reminder::getId, ids);
        int updates = reminderMapper.update(updateWrapper);
        if (updates == 0) {
            log.error("[log] 批量将用户的提醒消息标为未读失败 msg: {}", MessageConstant.RMINDER_NOT_FOUND);
            throw new NotFoundException(MessageConstant.RMINDER_NOT_FOUND);
        }
    }

    @Override
    public void deleteReminder(String id) {
        if (reminderMapper.deleteById(id) == 0) {
            log.error("[log] 删除用户的提醒消息失败 msg: {}", MessageConstant.RMINDER_NOT_FOUND);
            throw new NotFoundException(MessageConstant.RMINDER_NOT_FOUND);
        }
    }

    @Override
    public void batchDeleteReminders(List<String> ids) {
        int updates = reminderMapper.deleteBatchIds(ids);
        if (updates == 0) {
            log.error("[log] 批量删除用户的提醒消息失败 msg: {}", MessageConstant.RMINDER_NOT_FOUND);
            throw new NotFoundException(MessageConstant.RMINDER_NOT_FOUND);
        }
    }

}
