package org.anuen.advice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "org.anuen.api.client")
@MapperScan("org.anuen.advice.dao")
public class AdviceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdviceServiceApplication.class, args);
    }
}
