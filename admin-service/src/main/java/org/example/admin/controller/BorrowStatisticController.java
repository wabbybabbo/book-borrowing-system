package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.entity.BorrowStatistic;
import org.example.admin.service.IBorrowStatisticService;
import org.example.common.result.PageResult;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 借阅数量统计表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-05-05
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/borrow-statistic")
@Tag(name = "借阅数量统计相关接口")
public class BorrowStatisticController {

    private final IBorrowStatisticService borrowStatisticService;

    @GetMapping("/page")
    @Operation(summary = "分页查询借阅数量统计数据")
    public Result<PageResult<BorrowStatistic>> pageQuery(@ParameterObject PageQuery pageQuery) {
        log.info("[log] 分页查询借阅数量统计数据 {}", pageQuery);
        PageResult<BorrowStatistic> pageResult = borrowStatisticService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

}
