package org.example.admin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.Notice;
import org.example.admin.mapper.NoticeMapper;
import org.example.admin.service.INoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 通知消息表 服务实现类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

}
