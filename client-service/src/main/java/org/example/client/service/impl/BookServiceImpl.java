package org.example.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.entity.Book;
import org.example.client.entity.Category;
import org.example.client.entity.Publisher;
import org.example.client.mapper.BookMapper;
import org.example.client.mapper.CategoryMapper;
import org.example.client.mapper.PublisherMapper;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.BookVO;
import org.example.client.service.IBookService;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.NotFoundException;
import org.example.common.result.PageResult;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 书籍信息表 服务实现类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

    private final BookMapper bookMapper;
    private final CategoryMapper categoryMapper;
    private final PublisherMapper publisherMapper;

    @Override
    public PageResult<BookVO> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<Book> page = pageQuery.toMpPage();
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        List<String> filterConditions = pageQuery.getFilterConditions();
        log.info("[log] 书籍信息分页查询条件 filterConditions: {}", filterConditions);
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
        LambdaQueryWrapper<Book> lambdaQueryWrapper = queryWrapper.lambda()
                .select(Book::getId, Book::getCategoryId, Book::getName, Book::getImgUrl, Book::getIsbn, Book::getAuthor, Book::getPublisherId, Book::getDescription, Book::getStock);
        // 分页查询
        try {
            bookMapper.selectPage(page, lambdaQueryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 书籍信息分页查询失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.FIELD_NOT_FOUND);
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }
        List<Book> records = page.getRecords();

        // 查询所有书籍类别名称
        LambdaQueryWrapper<Category> queryWrapper1 = new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getName);
        List<Category> categories = categoryMapper.selectList(queryWrapper1);
        Map<String, String> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category.getName());
        }
        // 查询所有出版社名称
        LambdaQueryWrapper<Publisher> queryWrapper2 = new LambdaQueryWrapper<Publisher>()
                .select(Publisher::getId, Publisher::getName);
        List<Publisher> publishers = publisherMapper.selectList(queryWrapper2);
        Map<String, String> publisherMap = new HashMap<>();
        for (Publisher publisher : publishers) {
            publisherMap.put(publisher.getId(), publisher.getName());
        }

        //转化为VO
        List<BookVO> bookVOList = records.stream().map(book -> {
            BookVO bookVO = new BookVO();
            BeanUtil.copyProperties(book, bookVO);
            bookVO.setCategoryName(categoryMap.get(book.getCategoryId()));
            bookVO.setPublisherName(publisherMap.get(book.getPublisherId()));
            return bookVO;
        }).collect(Collectors.toList());

        return PageResult.<BookVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(bookVOList)
                .build();
    }

}
