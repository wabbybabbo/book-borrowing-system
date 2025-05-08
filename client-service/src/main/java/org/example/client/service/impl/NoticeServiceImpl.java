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
import org.example.client.entity.Notice;
import org.example.client.entity.SystemNotice;
import org.example.client.mapper.NoticeMapper;
import org.example.client.mapper.SystemNoticeMapper;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.NoticeVO;
import org.example.client.service.INoticeService;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.NotFoundException;
import org.example.common.result.PageResult;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户的通知消息表 服务实现类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-16
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    private final NoticeMapper noticeMapper;
    private final SystemNoticeMapper systemNoticeMapper;

    @Override
    public PageResult<NoticeVO> pageQuery(String id, PageQuery pageQuery) {
        // 构建分页查询条件
        Page<Notice> page = pageQuery.toMpPage();
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        List<String> filterConditions = pageQuery.getFilterConditions();
        log.info("[log] 用户通知消息分页查询条件 filterConditions: {}", filterConditions);
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
        LambdaQueryWrapper<Notice> lambdaQueryWrapper = queryWrapper.lambda()
                .select(Notice::getId, Notice::getTitle, Notice::getContent, Notice::getStatus, Notice::getCreateTime)
                .eq(Notice::getUserId, id);
        // 分页查询
        try {
            noticeMapper.selectPage(page, lambdaQueryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 用户通知消息分页查询失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.FIELD_NOT_FOUND);
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }
        List<Notice> records = page.getRecords();

        //转化为VO
        List<NoticeVO> noticeVOList = records.stream().map(notice -> {
            NoticeVO noticeVO = new NoticeVO();
            BeanUtil.copyProperties(notice, noticeVO);
            return noticeVO;
        }).collect(Collectors.toList());

        return PageResult.<NoticeVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(noticeVOList)
                .build();
    }

    @Override
    public Long getUnreadNoticeCount(String id) {
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<Notice>()
                .eq(Notice::getStatus, false)
                .eq(Notice::getUserId, id);;
        return noticeMapper.selectCount(queryWrapper);
    }

    @Override
    public void readNotice(String id) {
        Notice notice = new Notice();
        notice.setId(id);
        notice.setStatus(true);
        if (noticeMapper.updateById(notice) == 0) {
            log.error("[log] 将用户的通知消息标为已读失败 msg: {}", MessageConstant.USER_NOTICE_NOT_FOUND);
            throw new NotFoundException(MessageConstant.USER_NOTICE_NOT_FOUND);
        }
    }

    @Override
    public void batchReadNotices(List<String> ids) {
        LambdaUpdateWrapper<Notice> updateWrapper = new LambdaUpdateWrapper<Notice>()
                .set(Notice::getStatus, true)
                .in(Notice::getId, ids);
        int updates = noticeMapper.update(updateWrapper);
        if (updates == 0) {
            log.error("[log] 批量将用户的通知消息标为已读失败 msg: {}", MessageConstant.USER_NOTICE_NOT_FOUND);
            throw new NotFoundException(MessageConstant.USER_NOTICE_NOT_FOUND);
        }
    }

    @Override
    public void unreadNotice(String id) {
        Notice notice = new Notice();
        notice.setId(id);
        notice.setStatus(false);
        if (noticeMapper.updateById(notice) == 0) {
            log.error("[log] 将用户的通知消息标为未读失败 msg: {}", MessageConstant.USER_NOTICE_NOT_FOUND);
            throw new NotFoundException(MessageConstant.USER_NOTICE_NOT_FOUND);
        }
    }

    @Override
    public void batchUnreadNotices(List<String> ids) {
        LambdaUpdateWrapper<Notice> updateWrapper = new LambdaUpdateWrapper<Notice>()
                .set(Notice::getStatus, false)
                .in(Notice::getId, ids);
        int updates = noticeMapper.update(updateWrapper);
        if (updates == 0) {
            log.error("[log] 批量将用户的通知消息标为未读失败 msg: {}", MessageConstant.USER_NOTICE_NOT_FOUND);
            throw new NotFoundException(MessageConstant.USER_NOTICE_NOT_FOUND);
        }
    }

    @Override
    public void deleteNotice(String id) {
        if (noticeMapper.deleteById(id) == 0) {
            log.error("[log] 删除用户的通知消息失败 msg: {}", MessageConstant.USER_NOTICE_NOT_FOUND);
            throw new NotFoundException(MessageConstant.USER_NOTICE_NOT_FOUND);
        }
    }

    @Override
    public void batchDeleteNotices(List<String> ids) {
        int updates = noticeMapper.deleteBatchIds(ids);
        if (updates == 0) {
            log.error("[log] 批量删除用户的通知消息失败 msg: {}", MessageConstant.USER_NOTICE_NOT_FOUND);
            throw new NotFoundException(MessageConstant.USER_NOTICE_NOT_FOUND);
        }
    }

    @Override
    public void createNotice(String userId, String systemNoticeId) {
        // 查询系统通知消息
        LambdaQueryWrapper<SystemNotice> queryWrapper = new LambdaQueryWrapper<SystemNotice>()
                .select(SystemNotice::getTitle, SystemNotice::getContent)
                .eq(SystemNotice::getId, systemNoticeId);
        SystemNotice systemNotice = systemNoticeMapper.selectOne(queryWrapper);
        // 构建用户通知消息对象
        Notice notice = new Notice();
        BeanUtil.copyProperties(systemNotice, notice);
        notice.setUserId(userId);
        // 新增用户的通知消息
        noticeMapper.insert(notice);
    }

}
