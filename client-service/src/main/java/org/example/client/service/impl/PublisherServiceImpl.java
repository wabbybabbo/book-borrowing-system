package org.example.client.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.entity.Publisher;
import org.example.client.mapper.PublisherMapper;
import org.example.client.mapper.PublisherMapper;
import org.example.client.pojo.vo.PublisherVO;
import org.example.client.service.IPublisherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 出版社信息表 服务实现类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PublisherServiceImpl extends ServiceImpl<PublisherMapper, Publisher> implements IPublisherService {

    private final PublisherMapper publisherMapper;

    @Override
    public List<PublisherVO> getPublishers() {
        // 查询所有出版社名称
        return publisherMapper.getPublishers();
    }
}
