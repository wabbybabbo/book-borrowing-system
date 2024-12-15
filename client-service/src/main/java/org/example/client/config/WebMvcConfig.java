package org.example.client.config;

import lombok.extern.slf4j.Slf4j;
import org.example.client.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类，注册web层相关组件
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 注册自定义拦截器
     *
     * @param registry 拦截器的注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("[log] 注册自定义拦截器 UserInterceptor");

        registry.addInterceptor(new UserInterceptor())
                .excludePathPatterns("/**/user/login", "/**/api-docs"); //指定这个拦截器不应该拦截哪些路径。
    }

}
