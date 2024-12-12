package org.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.mapper.BookMapper;
import org.example.admin.mapper.CategoryMapper;
import org.example.admin.pojo.dto.CreateBookDTO;
import org.example.admin.pojo.dto.UpdateBookDTO;
import org.example.admin.pojo.entity.Book;
import org.example.admin.pojo.entity.Category;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.BookVO;
import org.example.admin.service.IBookService;
import org.example.common.api.client.CommonClient;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.AlreadyExistsException;
import org.example.common.exception.CheckException;
import org.example.common.exception.NotFoundException;
import org.example.common.exception.NullUpdateException;
import org.example.common.result.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 图书表 服务实现类
 * </p>
 *
 * @author wabbybabbo
 * @since 2024-04-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

    private final CommonClient commonClient;
    private final BookMapper bookMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public PageResult<BookVO> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<Book> page = pageQuery.toMpPage();
        QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>();
        List<String> filterConditions = pageQuery.getFilterConditions();
        log.info("[log] filterConditions: {}", filterConditions);
        if (null != filterConditions && !filterConditions.isEmpty()) {
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
            log.error("[log] BadSqlGrammarException: {}", e.getMessage());
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }
        List<Book> records = page.getRecords();

        // 查询所有图书类别名称
        QueryWrapper<Category> queryWrapper1 = new QueryWrapper<Category>()
                .select("id", "name");
        List<Category> categories = categoryMapper.selectList(queryWrapper1);
        Map<Integer, String> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category.getName());
        }

        //转化为VO
        List<BookVO> bookVOList = records.stream().map(book -> {
            BookVO bookVO = new BookVO();
            BeanUtils.copyProperties(book, bookVO);
            bookVO.setCategoryName(categoryMap.get(book.getCategoryId()));
            return bookVO;
        }).collect(Collectors.toList());

        return PageResult.<BookVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(bookVOList)
                .build();
    }

    @Override
    public void createBook(MultipartFile file, CreateBookDTO createBookDTO) {
        // 上传图书封面图片文件
        String url = commonClient.upload(file);
        // 构建图书对象
        Book book = new Book();
        BeanUtils.copyProperties(createBookDTO, book);
        book.setImgUrl(url);
        // 新增图书
        try {
            bookMapper.insert(book);
        } catch (DuplicateKeyException e) {
            log.error("[log] DuplicateKeyException: {}", e.getMessage());
            throw new AlreadyExistsException(MessageConstant.ISBN_ALREADY_EXISTS);
        }
    }

    @Override
    public void updateBook(MultipartFile file, UpdateBookDTO updateBookDTO) {
        String isbn = updateBookDTO.getIsbn();
        // 检查参数是否合法
        if (null != isbn && isbn.length() != 13) {
            log.info("[log] CheckException: {}", MessageConstant.INVALID_ISBN);
            throw new CheckException(MessageConstant.INVALID_ISBN);
        }
        // 构建图书对象
        Book book = new Book();
        BeanUtils.copyProperties(updateBookDTO, book);
        if (null != file) {
            // 上传图书封面图片文件
            String url = commonClient.upload(file);
            book.setImgUrl(url);
        }
        // 更改图书信息
        try {
            bookMapper.updateById(book);
        } catch (DuplicateKeyException e) {
            log.error("[log] DuplicateKeyException: {}", e.getMessage());
            throw new AlreadyExistsException(MessageConstant.ISBN_ALREADY_EXISTS);
        } catch (BadSqlGrammarException e) {
            log.error("[log] BadSqlGrammarException: {}", e.getMessage());
            throw new NullUpdateException(MessageConstant.UPDATE_FIELD_NOT_SET);
        }
    }

    @Override
    public void deleteBook(Integer id) {
        int updates = bookMapper.deleteById(id);
        if (0 == updates) {
            log.error("[log] NotFoundException: {}", MessageConstant.BOOK_NOT_FOUND);
            throw new NotFoundException(MessageConstant.BOOK_NOT_FOUND);
        }
    }

    @Override
    public void batchDeleteBooks(List<Integer> ids) {
        int updates = bookMapper.deleteBatchIds(ids);
        if (0 == updates) {
            log.error("[log] NotFoundException: {}", MessageConstant.BOOK_NOT_FOUND);
            throw new NotFoundException(MessageConstant.BOOK_NOT_FOUND);
        }
    }

}
