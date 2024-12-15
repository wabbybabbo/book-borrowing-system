package org.example.client.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//指定要扫描的Mapper接口类的包路径，这样就不需要在每一个mapper接口类上都添加@Mapper注解
@MapperScan("org.example.client.mapper")
public class MybatisPlusConfig {

    @Bean
    MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 创建分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 设置插件
        paginationInnerInterceptor.setMaxLimit(1000L);// 设置查询上限
        // 添加分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        return interceptor;
    }

}
