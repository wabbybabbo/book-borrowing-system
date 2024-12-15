package org.example.client.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.BookVO;
import org.example.client.service.IBookService;
import org.example.common.result.PageResult;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/page")
    public Result<PageResult<BookVO>> pageQuery(@ParameterObject PageQuery pageQuery) {
        log.info("[log] 分页查询书籍信息 {}", pageQuery);
        PageResult<BookVO> pageResult = bookService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

}
