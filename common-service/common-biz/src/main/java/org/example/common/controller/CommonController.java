package org.example.common.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.api.client.CommonClient;
import org.example.common.constant.MessageConstant;
import org.example.common.util.JwtUtil;
import org.example.common.util.QiniuOssUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/common")
@Tag(name = "公共接口")
public class CommonController implements CommonClient {

    private final JwtUtil jwtUtil;
    private final QiniuOssUtil qiniuOssUtil;


    @Override
    @Operation(summary = "打印请求路径中的变量")
    @GetMapping("/echo/{string}")
    public String echo(@PathVariable String string) {
        log.info("[log] PathVariable: {}", string);
        return string;
    }

    @Override
    @Operation(summary = "生成jwt令牌")
    @PostMapping("/token")
    public String createToken(@RequestBody Map<String, Object> claim) {
        return jwtUtil.createToken(claim);
    }

    /**
     * 文件上传
     *
     * @param file 上传文件
     * @return 文件的请求路径
     */
    @Override
    @Operation(summary = "文件上传")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE/*指定multipart/form-data*/)
    public String upload(@RequestPart("file") MultipartFile file) {
        log.info("[log] 文件上传: {}", file);

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //截取原始文件名的后缀 file-name.png
        String extension = null;
        if (originalFilename != null) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        //构造新文件名称
        String fileName = UUID.randomUUID() + extension;

        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            log.error("[log] IOException: {}", e.getMessage());
            throw new RuntimeException(MessageConstant.UPLOAD_FAILED);
        }
        //上传文件,并返回文件的请求路径
        return qiniuOssUtil.upload(fileBytes, fileName);
    }
}
