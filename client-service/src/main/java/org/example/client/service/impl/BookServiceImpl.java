package org.example.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.entity.Book;
import org.example.client.entity.Category;
import org.example.client.mapper.BookMapper;
import org.example.client.mapper.CategoryMapper;
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

    @Override
    public PageResult<BookVO> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<Book> page = pageQuery.toMpPage();
        QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>()
                .select("id", "category_id", "name", "img_url", "isbn", "author", "publisher", "description", "stock");
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
        // 分页查询
        try {
            bookMapper.selectPage(page, queryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 书籍信息分页查询失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.FIELD_NOT_FOUND);
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }
        List<Book> records = page.getRecords();
        // 查询所有书籍类别名称
        QueryWrapper<Category> queryWrapper1 = new QueryWrapper<Category>()
                .select("id", "name");
        List<Category> categories = categoryMapper.selectList(queryWrapper1);
        Map<Long, String> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category.getName());
        }
        //转化为VO
        List<BookVO> bookVOList = records.stream().map(book -> {
            BookVO bookVO = new BookVO();
            BeanUtil.copyProperties(book, bookVO);
            bookVO.setCategoryName(categoryMap.get(book.getCategoryId()));
            return bookVO;
        }).collect(Collectors.toList());

        return PageResult.<BookVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(bookVOList)
                .build();
    }

}
