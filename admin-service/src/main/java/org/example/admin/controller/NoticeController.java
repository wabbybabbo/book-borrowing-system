package org.example.admin.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.service.INoticeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 通知消息表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/notice")
@Tag(name = "通知消息相关接口")
public class NoticeController {

    private final INoticeService noticeService;

}
