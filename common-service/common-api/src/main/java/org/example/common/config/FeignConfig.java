package org.example.common.config;

import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import org.example.common.client.fallback.CommonClientFallbackFactory;
import org.springframework.context.annotation.Bean;

/**
 * 全局配置：
 * 在启动类中的注解@EnableFeignClients中，添加defaultConfiguration来指定该服务中所有OpenFeign接口使用的配置类
 * 比如，@EnableFeignClients(basePackages = "org.example.common.client", defaultConfiguration = config.org.example.common.config.FeignConfig.class)
 * 局部配置：
 * 在OpenFeign接口中的@FeignClient注解中，添加configuration来指定该OpenFeign接口使用的配置类
 * 比如，@FeignClient(value = "provider-service", configuration = config.org.example.common.config.FeignConfig.class)
 */
@Slf4j
public class FeignConfig {

    /**
     * OpenFeign日志级别：
     * NONE: 不记录任何日志，是OpenFeign默认日志级别（性能最佳，适用于生产环境）
     * BASIC: 仅记录请求方法、URL、响应状态码、执行时间（适用于生产环境追踪问题）
     * HEADERS: 在记录BASIC级别的基础上，记录请求和响应的header头部信息
     * FULL: 记录请求响应的header、body和元数据（适用于开发和测试环境定位问题）
     * <p>
     * 通过Logger.Level设置，可以控制Feign生成的日志信息的多少，但具体的日志的输出是由你的日志框架配置决定的。
     * 当日志配置文件（比如logback.xml或application.properties）中为Feign客户端设置的日志级别为INFO时，这意味着只会输出INFO等级的日志消息。
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        log.info("[log] 设置 OpenFeign 日志级别为 FULL");
        return Logger.Level.FULL;
    }

    @Bean
    public CommonClientFallbackFactory CommonClientFallbackFactory() {
        log.info("[log] 设置 CommonClient 的回退策略工厂");
        return new CommonClientFallbackFactory();
    }

}
