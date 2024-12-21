package org.example.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient //开启服务注册发现功能
@EnableFeignClients(basePackages = "org.example.common.client") //开启Feign功能，basePackages指定Feign接口所在的包路径
@EnableTransactionManagement //开启注解方式的事务管理
@EnableScheduling //开启任务调度
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
