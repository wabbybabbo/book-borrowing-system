package org.example.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient //开启服务注册发现功能
//@RefreshScope //通过 Spring Cloud 原生注解 @RefreshScope 实现配置自动更新
@MapperScan("org.example.common.mapper") // //指定要扫描的Mapper接口类的包路径，这样就不需要在每一个mapper接口类上都添加@Mapper注解
public class CommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
    }

}
