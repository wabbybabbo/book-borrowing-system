package org.example.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.admin.entity.Publisher;
import org.example.admin.pojo.dto.BatchDTO;
import org.example.admin.pojo.dto.CreatePublisherDTO;
import org.example.admin.pojo.dto.UpdatePublisherDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.PublisherVO;
import org.example.admin.service.IPublisherService;
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
 * 出版社信息表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/publisher")
@Tag(name = "书籍出版社相关接口")
public class PublisherController {
    
    private final IPublisherService publisherService;

    @GetMapping("/page")
    @Cacheable(cacheNames = "publisherCache",
            key = "'admin-service' + ':' + 'publisherList' + ':' + #pageQuery.current + ':' + #pageQuery.size",
            /*只有当PageQuery对象中的filterConditions和sortBy都为null时才会进行缓存*/
            condition = "#pageQuery.filterConditions.empty && #pageQuery.sortBy.blank")
    @Operation(summary = "分页查询出版社信息")
    public Result<PageResult<Publisher>> pageQuery(@ParameterObject PageQuery pageQuery) {
        PageResult<Publisher> pageResult = publisherService.pageQuery(pageQuery);
        return Result.success(pageResult);
    }

    @GetMapping
    @Cacheable(cacheNames = "publisherCache", key = "'admin-service' + ':' + 'publisherVOList'")
    @Operation(summary = "查询所有出版社信息")
    public Result<List<PublisherVO>> getPublishers() {
        List<PublisherVO> categories = publisherService.getPublishers();
        return Result.success(categories);
    }

    @PostMapping
    @CacheEvict(cacheNames = "publisherCache", allEntries = true)
    @Operation(summary = "新建出版社信息")
    public Result<Object> createPublisher(@RequestBody @Valid CreatePublisherDTO createPublisherDTO) {
        publisherService.createPublisher(createPublisherDTO);
        return Result.success(MessageConstant.CREATE_SUCCESS);
    }

    @PutMapping
    @CacheEvict(cacheNames = {"publisherCache", "bookCache"}, allEntries = true)
    @Operation(summary = "更改出版社信息")
    public Result<Object> updatePublisher(@RequestBody @Valid UpdatePublisherDTO updatePublisherDTO) {
        publisherService.updatePublisher(updatePublisherDTO);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    @DeleteMapping
    @CacheEvict(cacheNames = "publisherCache", allEntries = true)
    @Operation(summary = "删除出版社信息")
    public Result<Object> deletePublisher(
            @RequestParam
            @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
            @Parameter(description = "出版社ID", required = true)
            String id) {
        publisherService.deletePublisher(id);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    @DeleteMapping("/batch")
    @CacheEvict(cacheNames = "publisherCache", allEntries = true)
    @Operation(summary = "批量删除出版社信息")
    public Result<Object> batchDeletePublishers(
            @RequestBody @Valid
            BatchDTO batchDTO) {
        publisherService.batchDeletePublishers(batchDTO.getIds());
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }
    
}
