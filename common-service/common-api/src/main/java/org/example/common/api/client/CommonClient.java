package org.example.common.api.client;


import org.example.common.api.client.fallback.CommonClientFallbackFactory;
import org.example.common.api.config.FeignConfig;
import org.example.common.api.config.FeignSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * FeignClient注解属性说明：
 * name等同value 指定被调用的服务名(spring.application.name)
 * path 定义访问接口时的统一前缀
 * fallbackFactory定义当调用远程服务失败时的回退策略
 */
@FeignClient(
        name = "common-service",
        path = "/common",
        fallbackFactory = CommonClientFallbackFactory.class,
        configuration = {FeignConfig.class, FeignSupportConfig.class}
)
public interface CommonClient {

    //http://common-service的ip地址/common/echo/{string}
    @GetMapping("/echo/{string}")
    String echo(@PathVariable("string") String string);

    /**
     * 生成 JWT令牌
     *
     * @param claim 载荷
     * @return JWT token
     */
    @PostMapping("/token")
    String createToken(@RequestBody Map<String, Object> claim);

    /**
     * 文件上传
     *
     * @param file 上传文件
     * @return 文件的请求路径
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE/*指定multipart/form-data*/)
    String upload(@RequestPart("file") MultipartFile file);

}
