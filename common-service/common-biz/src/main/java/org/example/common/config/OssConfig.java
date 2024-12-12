package org.example.common.config;

import lombok.extern.slf4j.Slf4j;
import org.example.common.properties.QiniuOssProperties;
import org.example.common.util.QiniuOssUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建OssUtil对象
 * 在配置类中，开发者可以通过使用@Bean注解来定义Bean。
 * 当Spring容器启动时，它会扫描这些配置类，并自动调用被@Bean标注的方法，将该方法返回的对象注册为一个Bean实例到Spring的应用上下文中。
 */
@Configuration
@Slf4j
public class OssConfig {

    @Bean
    public QiniuOssUtil qiniuOssUtil(QiniuOssProperties qiniuOssProperties) {
        log.info("[log] 开始创建七牛云文件上传工具类对象：{}", qiniuOssProperties);
        return QiniuOssUtil.builder()
                .accessKey(qiniuOssProperties.getAccessKey())
                .secretKey(qiniuOssProperties.getSecretKey())
                .bucket(qiniuOssProperties.getBucket())
                .cdn(qiniuOssProperties.getCdn())
                .build();
    }

}
