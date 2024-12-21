package org.example.client.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.pojo.dto.CreateBorrowDTO;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.BorrowVO;
import org.example.client.service.IBorrowService;
import org.example.common.constant.ClaimConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.result.PageResult;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 借阅记录表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/borrow")
@Tag(name = "书籍借阅相关接口")
public class BorrowController {

    private final IBorrowService borrowService;

    @Operation(summary = "分页查询用户的借阅记录")
    @GetMapping("/page")
    public Result<PageResult<BorrowVO>> pageQuery(
            @ParameterObject
            PageQuery pageQuery,
            @Parameter(description = "用户ID")
            @RequestHeader(ClaimConstant.CLIENT_ID)
            String id
    ) {
        log.info("[log] 分页查询用户的借阅记录 {}, id: {}", pageQuery, id);
        PageResult<BorrowVO> pageResult = borrowService.pageQuery(pageQuery, id);
        return Result.success(pageResult);
    }

//    @Operation(summary = "查询用户所有的借阅记录")
//    @GetMapping
//    public Result<List<BorrowVO>> getBorrows() {
//        log.info("[log] 查询用户所有的借阅记录");
//        List<BorrowVO> borrows = borrowService.getBorrows();
//        return Result.success(borrows);
//    }

    @Operation(summary = "新增用户的借阅预约记录")
    @PostMapping
    public Result<Object> createBorrow(
            @RequestBody @Valid
            CreateBorrowDTO createBorrowDTO,
            @Parameter(description = "用户ID", required = true)
            @RequestHeader(ClaimConstant.CLIENT_ID)
            String id
    ) {
        log.info("[log] 新增用户的借阅预约记录 {}, id: {}", createBorrowDTO, id);
        borrowService.createBorrow(createBorrowDTO, id);
        return Result.success(MessageConstant.BORROW_SUCCESS);
    }

    @Operation(summary = "取消书籍借阅预约")
    @PutMapping
    public Result<Object> cancelBorrow(
            @Parameter(description = "借阅记录ID", required = true)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            Long id
    ) {
        log.info("[log] 取消书籍借阅预约 id: {}", id);
        borrowService.cancelBorrow(id);
        return Result.success(MessageConstant.CANCEL_SUCCESS);
    }

    @Operation(summary = "删除书籍借阅记录")
    @DeleteMapping
    public Result<Object> deleteBorrow(
            @Parameter(description = "借阅记录ID", required = true)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            Long id
    ) {
        log.info("[log] 删除书籍借阅记录 id: {}", id);
        borrowService.deleteBorrow(id);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

}
