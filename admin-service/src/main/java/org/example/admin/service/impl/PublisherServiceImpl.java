package org.example.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.Publisher;
import org.example.admin.mapper.PublisherMapper;
import org.example.admin.pojo.dto.CreatePublisherDTO;
import org.example.admin.pojo.dto.UpdatePublisherDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.PublisherVO;
import org.example.admin.service.IPublisherService;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.AlreadyExistsException;
import org.example.common.exception.NotAllowedException;
import org.example.common.exception.NotFoundException;
import org.example.common.result.PageResult;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
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
    public PageResult<Publisher> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<Publisher> page = pageQuery.toMpPage();
        QueryWrapper<Publisher> queryWrapper = new QueryWrapper<>();
        List<String> filterConditions = pageQuery.getFilterConditions();
        log.info("[log] 出版社信息分页查询条件 filterConditions: {}", filterConditions);
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
            publisherMapper.selectPage(page, queryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 出版社信息分页查询失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.FIELD_NOT_FOUND);
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }

        return PageResult.<Publisher>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(page.getRecords())
                .build();
    }

    @Override
    public void createPublisher(CreatePublisherDTO createPublisherDTO) {
        // 构建出版社对象
        Publisher publisher = new Publisher();
        publisher.setName(createPublisherDTO.getName());
        // 新增出版社信息
        try {
            publisherMapper.insert(publisher);
        } catch (DuplicateKeyException e) {
            log.error("[log] 新增出版社信息失败 DuplicateKeyException: {}, msg: {}", e.getMessage(), MessageConstant.PUBLISHER_ALREADY_EXISTS);
            throw new AlreadyExistsException(MessageConstant.PUBLISHER_ALREADY_EXISTS);
        }
    }

    @Override
    public List<PublisherVO> getPublishers() {
        // 查询所有出版社名称
        return publisherMapper.getPublishers();
    }

    @Override
    public void updatePublisher(UpdatePublisherDTO updatePublisherDTO) {
        // 构建出版社对象
        Publisher publisher = new Publisher();
        BeanUtil.copyProperties(updatePublisherDTO, publisher);
        // 更改出版社信息
        try {
            publisherMapper.updateById(publisher);
        } catch (DuplicateKeyException e) {
            log.error("[log] 更改出版社信息失败 DuplicateKeyException: {}, msg: {}", e.getMessage(), MessageConstant.PUBLISHER_ALREADY_EXISTS);
            throw new AlreadyExistsException(MessageConstant.PUBLISHER_ALREADY_EXISTS);
        }
    }

    @Override
    public void deletePublisher(String id) {
        // 删除出版社信息
        try {
            publisherMapper.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            log.error("[log] 删除出版社信息失败 DataIntegrityViolationException: {}, msg: {}", e.getMessage(), MessageConstant.PUBLISHER_ALREADY_EXISTS);
            throw new NotAllowedException(MessageConstant.BOOK_REFERENCES_PUBLISHER);
        }
    }

    @Override
    public void batchDeletePublishers(List<String> ids) {
        // 批量删除出版社信息
        try {
            publisherMapper.deleteBatchIds(ids);
        } catch (DataIntegrityViolationException e) {
            log.error("[log] 批量删除出版社信息失败 DataIntegrityViolationException: {}, msg: {}", e.getMessage(), MessageConstant.PUBLISHER_ALREADY_EXISTS);
            throw new NotAllowedException(MessageConstant.BOOK_REFERENCES_PUBLISHER);
        }
    }

}
