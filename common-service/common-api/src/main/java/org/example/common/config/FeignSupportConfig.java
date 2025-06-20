package org.example.common.config;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class FeignSupportConfig {

    @Bean
    public Encoder multipartFormEncoder() {
        log.info("[log] 为 OpenFeign 创建所需的编码器，使其支持传递 MultipartFile 类型参数");
        return new SpringFormEncoder(new SpringEncoder(() ->
                new HttpMessageConverters(new RestTemplate().getMessageConverters()))
        );
    }

}
