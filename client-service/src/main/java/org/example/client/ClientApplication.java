package org.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient //开启服务注册发现功能
//开启OpenFeign功能，basePackages指定Feign接口所在的包路径
@EnableFeignClients(basePackages = "org.example.common.api.client")
@EnableTransactionManagement //开启注解方式的事务管理
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

}
