package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.pojo.dto.CreateBookDTO;
import org.example.admin.pojo.dto.UpdateBookDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.BookVO;
import org.example.admin.service.IBookService;
import org.example.common.constant.MessageConstant;
import org.example.common.result.PageResult;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 书籍信息表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/book")
@Tag(name = "书籍相关接口")
public class BookController {

    private final IBookService bookService;

    @Operation(summary = "分页查询书籍信息")
    //只有当PageQuery对象中的filterConditions和sortBy都为null时才会进行缓存
    @Cacheable(cacheNames = "bookCache", key = "'bookList'+':'+#pageQuery.current+':'+#pageQuery.size", condition = "#pageQuery.filterConditions.empty && #pageQuery.sortBy.blank")
    @GetMapping("/page")
    public Result<PageResult<BookVO>> pageQuery(@ParameterObject PageQuery pageQuery) {
        log.info("[log] 分页查询书籍信息 {}", pageQuery);
        PageResult<BookVO> pageResult = bookService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

    @Operation(summary = "新建书籍信息")
    @CacheEvict(cacheNames = "bookCache", allEntries = true)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE/*指定multipart/form-data*/)
    public Result createBook(
            @Parameter(description = "书籍封面图片文件")
            @RequestPart("file")
            @NotNull(message = MessageConstant.FIELD_NOT_BLANK)
            MultipartFile file,
            @RequestPart
            @Valid
            CreateBookDTO createBookDTO) {
        log.info("[log] 新建书籍信息 {}", createBookDTO);
        bookService.createBook(file, createBookDTO);
        return Result.success(MessageConstant.CREATE_SUCCESS);
    }

    @Operation(summary = "更改书籍信息")
    @CacheEvict(cacheNames = "bookCache", allEntries = true)
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE/*指定multipart/form-data*/)
    public Result updateBook(
            @Parameter(description = "书籍封面图片文件")
            @RequestPart(value = "file", required = false)
            MultipartFile file,
            @RequestPart
            @Valid
            UpdateBookDTO updateBookDTO) {
        log.info("[log] 更改书籍信息 {}", updateBookDTO);
        bookService.updateBook(file, updateBookDTO);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除书籍信息")
    @CacheEvict(cacheNames = "bookCache", allEntries = true)
    @DeleteMapping
    public Result deleteBook(
            @Parameter(description = "书籍ID")
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            Long id
    ) {
        log.info("[log] 删除书籍信息 id: {}", id);
        bookService.deleteBook(id);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    @Operation(summary = "批量删除书籍信息")
    @CacheEvict(cacheNames = "bookCache", allEntries = true)
    @DeleteMapping("/batch")
    public Result batchDeleteBooks(
            @RequestBody
            @NotEmpty(message = MessageConstant.FIELD_NOT_EMPTY)
            List<Long> ids
    ) {
        log.info("[log] 批量删除书籍信息 ids: {}", ids);
        bookService.batchDeleteBooks(ids);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

}
