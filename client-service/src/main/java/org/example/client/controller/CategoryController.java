package org.example.client.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.pojo.vo.CategoryVO;
import org.example.client.service.ICategoryService;
import org.example.common.result.Result;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 图书类别表 前端控制器
 * </p>
 *
 * @author wabbybabbo
 * @since 2024-04-07
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
@Tag(name = "图书类别相关接口")
public class CategoryController {

    private final ICategoryService categoryService;

    @Operation(summary = "查询所有图书类别项目")
    @Cacheable(cacheNames = "categoryCache", key = "'categoryVOList'")
    @GetMapping
    public Result<List<CategoryVO>> getCategories() {
        log.info("[log] 查询所有图书类别项目");
        List<CategoryVO> categories = categoryService.getCategories();
        return Result.success(categories);
    }

}
