package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.admin.pojo.dto.BatchDTO;
import org.example.admin.pojo.dto.CreateBookDTO;
import org.example.admin.pojo.dto.UpdateBookDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.BookVO;
import org.example.admin.service.IBookService;
import org.example.common.constant.MessageConstant;
import org.example.common.result.PageResult;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 书籍信息表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/book")
@Tag(name = "书籍相关接口")
public class BookController {

    private final IBookService bookService;

    @GetMapping("/page")
    @Operation(summary = "分页查询书籍信息")
    public Result<PageResult<BookVO>> pageQuery(@ParameterObject PageQuery pageQuery) {
        PageResult<BookVO> pageResult = bookService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE /*指定multipart/form-data*/)
    @Operation(summary = "新建书籍信息")
    public Result<Object> createBook(
            @RequestPart("file")
            @NotNull(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "书籍封面图片文件", required = true)
            MultipartFile file,
            @RequestPart @Valid
            CreateBookDTO createBookDTO) {
        bookService.createBook(file, createBookDTO);
        return Result.success(MessageConstant.CREATE_SUCCESS);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE /*指定multipart/form-data*/)
    @Operation(summary = "更改书籍信息")
    public Result<Object> updateBook(
            @RequestPart(value = "file", required = false)
            @Parameter(description = "书籍封面图片文件")
            MultipartFile file,
            @RequestPart @Valid
            UpdateBookDTO updateBookDTO) {
        bookService.updateBook(file, updateBookDTO);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    @DeleteMapping
    @Operation(summary = "删除书籍信息")
    public Result<Object> deleteBook(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "书籍ID", required = true)
            String id) {
        bookService.deleteBook(id);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除书籍信息")
    public Result<Object> batchDeleteBooks(
            @RequestBody @Valid
            BatchDTO batchDTO) {
        bookService.batchDeleteBooks(batchDTO.getIds());
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

}
