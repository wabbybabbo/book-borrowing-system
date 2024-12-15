package org.example.common.config;

import lombok.extern.slf4j.Slf4j;
import org.example.common.properties.JwtProperties;
import org.example.common.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建OssUtil对象
 * 在配置类中，开发者可以通过使用@Bean注解来定义Bean。
 * 当Spring容器启动时，它会扫描这些配置类，并自动调用被@Bean标注的方法，将该方法返回的对象注册为一个Bean实例到Spring的应用上下文中。
 */
@Configuration
@Slf4j
public class JwtConfig {

    @Bean
    public JwtUtil jwtUtil(JwtProperties JwtProperties) {
        log.info("[log] 创建JWT工具类对象 {}", JwtProperties);
        return JwtUtil.builder()
                .key(JwtProperties.getKey())
                .ttl(JwtProperties.getTtl())
                .build();
    }

}
