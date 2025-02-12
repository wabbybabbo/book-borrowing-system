package org.example.client.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.pojo.vo.PublisherVO;
import org.example.client.service.IPublisherService;
import org.example.common.result.Result;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 出版社信息表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/publisher")
@Tag(name = "书籍出版社相关接口")
public class PublisherController {
    private final IPublisherService publisherService;

    @GetMapping
    @Cacheable(cacheNames = "publisherCache", key = "'client-service' + ':' + 'publisherVOList'")
    @Operation(summary = "查询所有出版社名称")
    public Result<List<PublisherVO>> getPublishers() {
        log.info("[log] 查询所有出版社名称");
        List<PublisherVO> publishers = publisherService.getPublishers();
        return Result.success(publishers);
    }
}
