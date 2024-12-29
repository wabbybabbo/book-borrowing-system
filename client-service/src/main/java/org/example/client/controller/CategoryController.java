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
 * 书籍类别表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
@Tag(name = "书籍类别相关接口")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping
    @Cacheable(cacheNames = "categoryCache", key = "'client-service' + ':' + 'categoryVOList'")
    @Operation(summary = "查询所有书籍类别")
    public Result<List<CategoryVO>> getCategories() {
        log.info("[log] 查询所有书籍类别");
        List<CategoryVO> categories = categoryService.getCategories();
        return Result.success(categories);
    }

}
