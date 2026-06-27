package com.example.videobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 开启事务管理（SpringBoot 2.x+ 可省略，但显式添加更稳妥）
public class VideoBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoBackendApplication.class, args);
    }
}