package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.Borrow;
import org.example.admin.pojo.dto.ReturnRegisterDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.service.IBorrowService;
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
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/borrow")
@Tag(name = "书籍借阅相关接口")
public class BorrowController {

    private final IBorrowService borrowService;

    @GetMapping("/page")
    @Operation(summary = "分页查询借阅记录")
    public Result<PageResult<Borrow>> pageQuery(@ParameterObject PageQuery pageQuery) {
        log.info("[log] 分页查询借阅记录 {}", pageQuery);
        PageResult<Borrow> pageResult = borrowService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

    @GetMapping
    @Operation(summary = "查询用户的所有借阅记录")
    public Result<List<Borrow>> getBorrows(
            @Parameter(description = "用户ID")
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            String id) {
        log.info("[log] 查询用户的所有借阅记录，id: {}", id);
        List<Borrow> borrows = borrowService.getBorrows(id);
        return Result.success(borrows);
    }

    @PutMapping("/register/borrowing")
    @Operation(summary = "借阅登记")
    public Result<Object> borrowRegister(
            @RequestParam
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "书籍借阅记录ID", required = true)
            String id) {
        log.info("[log] 借阅登记 id: {}", id);
        borrowService.borrowRegister(id);
        return Result.success(MessageConstant.BORROW_REGISTER_SUCCESS);
    }

    @PutMapping("/register/returned")
    @Operation(summary = "归还登记")
    public Result<Object> returnRegister(@RequestBody @Valid ReturnRegisterDTO returnRegisterDTO) {
        log.info("[log] 归还登记 {}", returnRegisterDTO);
        borrowService.returnRegister(returnRegisterDTO);
        return Result.success(MessageConstant.RETURN_REGISTER_SUCCESS);
    }

    @PutMapping("/task/update-borrows")
    @Operation(summary = "触发定时任务-更新借阅记录的状态")
    public void updateBorrows() {
        log.info("[log] 触发定时任务-更新借阅记录的状态");
        borrowService.updateBorrowingBorrows();
        borrowService.updateReservedBorrows();
    }

}
