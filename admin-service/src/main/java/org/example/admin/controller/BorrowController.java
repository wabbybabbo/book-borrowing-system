package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.pojo.dto.ReturnRegisterDTO;
import org.example.admin.pojo.entity.Borrow;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.service.IBorrowService;
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
 * @author wabbybabbo
 * @since 2024-04-07
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/borrow")
@Tag(name = "图书借阅相关接口")
public class BorrowController {

    private final IBorrowService borrowService;

    @Operation(summary = "分页查询借阅记录")
    @GetMapping("/page")
    public Result<PageResult<Borrow>> pageQuery(@ParameterObject PageQuery pageQuery) {
        log.info("[log] 分页查询借阅记录 pageQuery: {}", pageQuery);
        PageResult<Borrow> pageResult = borrowService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

//    @Operation(summary = "查询用户的借阅记录")
//    @GetMapping
//    public Result<List<Borrow>> getBorrows(
//            @Parameter(description = "用户ID")
//            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
//            Integer id
//    ) {
//        log.info("[log] 查询用户的借阅记录，用户ID：{}", id);
//        List<Borrow> borrows = borrowService.getBorrows(id);
//        return Result.success(borrows);
//    }

    @Operation(summary = "借阅登记")
    @PutMapping
    public Result borrowRegister(
            @Parameter(description = "图书借阅记录ID")
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            Integer id
    ) {
        log.info("[log] borrowId: {}", id);
        borrowService.borrowRegister(id);
        return Result.success();
    }

    @Operation(summary = "归还登记")
    @PutMapping("/return")
    public Result returnRegister(@RequestBody @Valid ReturnRegisterDTO returnRegisterDTO) {
        log.info("[log] returnRegisterDTO: {}", returnRegisterDTO);
        borrowService.returnRegister(returnRegisterDTO);
        return Result.success();
    }

}
