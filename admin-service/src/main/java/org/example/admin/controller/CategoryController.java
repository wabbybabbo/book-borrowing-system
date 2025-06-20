package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.admin.entity.Category;
import org.example.admin.pojo.dto.BatchDTO;
import org.example.admin.pojo.dto.CreateCategoryDTO;
import org.example.admin.pojo.dto.UpdateCategoryDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.CategoryVO;
import org.example.admin.service.ICategoryService;
import org.example.common.constant.MessageConstant;
import org.example.common.result.PageResult;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 书籍类别表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
@Tag(name = "书籍类别相关接口")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/page")
    @Cacheable(cacheNames = "categoryCache",
            key = "'admin-service' + ':' + 'categoryList' + ':' + #pageQuery.current + ':' + #pageQuery.size",
            /*只有当PageQuery对象中的filterConditions和sortBy都为null时才会进行缓存*/
            condition = "#pageQuery.filterConditions.empty && #pageQuery.sortBy.blank")
    @Operation(summary = "分页查询书籍类别")
    public Result<PageResult<Category>> pageQuery(@ParameterObject PageQuery pageQuery) {
        PageResult<Category> pageResult = categoryService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

    @GetMapping
    @Cacheable(cacheNames = "categoryCache", key = "'admin-service' + ':' + 'categoryVOList'")
    @Operation(summary = "查询所有书籍类别")
    public Result<List<CategoryVO>> getCategories() {
        List<CategoryVO> categories = categoryService.getCategories();
        return Result.success(categories);
    }

    @PostMapping
    @CacheEvict(cacheNames = "categoryCache", allEntries = true)
    @Operation(summary = "新建书籍类别")
    public Result<Object> createCategory(@RequestBody @Valid CreateCategoryDTO createCategoryDTO) {
        categoryService.createCategory(createCategoryDTO);
        return Result.success(MessageConstant.CREATE_SUCCESS);
    }

    @PutMapping
    @CacheEvict(cacheNames = {"categoryCache", "bookCache"}, allEntries = true)
    @Operation(summary = "更改书籍类别信息")
    public Result<Object> updateCategory(@RequestBody @Valid UpdateCategoryDTO updateCategoryDTO) {
        categoryService.updateCategory(updateCategoryDTO);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    @DeleteMapping
    @CacheEvict(cacheNames = "categoryCache", allEntries = true)
    @Operation(summary = "删除书籍类别")
    public Result<Object> deleteCategory(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "书籍类别ID", required = true)
            String id) {
        categoryService.deleteCategory(id);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    @DeleteMapping("/batch")
    @CacheEvict(cacheNames = "categoryCache", allEntries = true)
    @Operation(summary = "批量删除书籍类别")
    public Result<Object> batchDeleteCategories(
            @RequestBody @Valid
            BatchDTO batchDTO) {
        categoryService.batchDeleteCategories(batchDTO.getIds());
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

}
