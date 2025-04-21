package org.example.client.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.client.pojo.dto.BatchDTO;
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

import java.util.List;

/**
 * <p>
 * 借阅记录表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/borrow")
@Tag(name = "书籍借阅相关接口")
public class BorrowController {

    private final IBorrowService borrowService;

    @GetMapping("/page")
    @Operation(summary = "分页查询用户的借阅记录")
    public Result<PageResult<BorrowVO>> pageQuery(
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "用户ID", required = true, hidden = true)
            String id,
            @ParameterObject
            PageQuery pageQuery) {
        PageResult<BorrowVO> pageResult = borrowService.pageQuery(id, pageQuery);
        return Result.success(pageResult);
    }

    @GetMapping
    @Operation(summary = "查询用户的所有借阅记录")
    public Result<List<BorrowVO>> getBorrows(
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "用户ID", required = true, hidden = true)
            String id) {
        List<BorrowVO> borrows = borrowService.getBorrows(id);
        return Result.success(borrows);
    }

    @PostMapping
    @Operation(summary = "新增用户的借阅预约记录")
    public Result<Object> createBorrow(
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "用户ID", required = true, hidden = true)
            String id,
            @RequestBody @Valid
            CreateBorrowDTO createBorrowDTO) {
        borrowService.createBorrow(id, createBorrowDTO);
        return Result.success(MessageConstant.BORROW_SUCCESS);
    }

    @PutMapping
    @Operation(summary = "取消书籍借阅预约")
    public Result<Object> cancelBorrow(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "借阅记录ID", required = true)
            String id) {
        borrowService.cancelBorrow(id);
        return Result.success(MessageConstant.CANCEL_SUCCESS);
    }

    @DeleteMapping
    @Operation(summary = "删除书籍借阅记录")
    public Result<Object> deleteBorrow(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "借阅记录ID", required = true)
            String id) {
        borrowService.deleteBorrow(id);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除书籍借阅记录")
    public Result<Object> batchDeleteBorrows(
            @RequestBody @Valid
            BatchDTO batchDTO) {
        borrowService.batchDeleteBorrows(batchDTO.getIds());
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

}
