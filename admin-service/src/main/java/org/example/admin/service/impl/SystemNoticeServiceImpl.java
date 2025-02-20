package org.example.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.SystemNotice;
import org.example.admin.mapper.SystemNoticeMapper;
import org.example.admin.pojo.dto.CreateSystemNoticeDTO;
import org.example.admin.pojo.dto.UpdateSystemNoticeDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.service.ISystemNoticeService;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RabbitMQConstant;
import org.example.common.exception.NotAllowedException;
import org.example.common.exception.NotFoundException;
import org.example.common.result.PageResult;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统通知消息表 服务实现类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-16
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SystemNoticeServiceImpl extends ServiceImpl<SystemNoticeMapper, SystemNotice> implements ISystemNoticeService {

    private final SystemNoticeMapper systemNoticeMapper;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public PageResult<SystemNotice> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<SystemNotice> page = pageQuery.toMpPage();
        QueryWrapper<SystemNotice> queryWrapper = new QueryWrapper<>();
        List<String> filterConditions = pageQuery.getFilterConditions();
        log.info("[log] 系统通知消息分页查询条件 filterConditions: {}", filterConditions);
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
        // 分页查询
        try {
            systemNoticeMapper.selectPage(page, queryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 系统通知消息分页查询失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.FIELD_NOT_FOUND);
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }

        return PageResult.<SystemNotice>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(page.getRecords())
                .build();
    }

    @Override
    public void createSystemNotice(CreateSystemNoticeDTO createSystemNoticeDTO) {
        // 构建系统通知消息对象
        SystemNotice systemNotice = new SystemNotice();
        BeanUtil.copyProperties(createSystemNoticeDTO, systemNotice);
        // 新增系统通知消息
        systemNoticeMapper.insert(systemNotice);
    }

    @Override
    public void updateSystemNotice(UpdateSystemNoticeDTO updateSystemNoticeDTO) {
        // 构建系统通知消息对象
        SystemNotice systemNotice = new SystemNotice();
        BeanUtil.copyProperties(updateSystemNoticeDTO, systemNotice);
        // 更改系统通知消息
        systemNoticeMapper.updateById(systemNotice);
    }

    @Override
    public void deleteSystemNotice(String id) {
        // 删除系统通知消息
        systemNoticeMapper.deleteById(id);
    }

    @Override
    public void batchDeleteSystemNotices(List<String> ids) {
        // 批量删除系统通知消息
        systemNoticeMapper.deleteBatchIds(ids);
    }

    @Override
    public void publishSystemNotice(String id) {
        LambdaQueryWrapper<SystemNotice> queryWrapper = new LambdaQueryWrapper<SystemNotice>()
                .select(SystemNotice::getStatus)
                .eq(SystemNotice::getId, id);
        SystemNotice systemNotice = systemNoticeMapper.selectOne(queryWrapper);

        if(systemNotice.getStatus()){
            throw new NotAllowedException(MessageConstant.PUBLISH_IS_NOT_ALLOWED);
        }

        LambdaUpdateWrapper<SystemNotice> updateWrapper = new LambdaUpdateWrapper<SystemNotice>()
                .set(SystemNotice::getStatus, true)
                .eq(SystemNotice::getId, id);
        systemNoticeMapper.update(updateWrapper);

        // 发送消息，触发前端的消息通知
        rabbitTemplate.convertAndSend(RabbitMQConstant.NOTICE_FANOUT_EXCHANGE, null, id);
    }

}
