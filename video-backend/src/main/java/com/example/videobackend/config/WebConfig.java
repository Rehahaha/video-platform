package com.example.videobackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${video.storage.base-path}")
    private String basePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将本地磁盘目录映射为 /static/** 访问路径
        // 例如：/static/videos/xxx.mp4 -> 实际访问 E:/video-storage/videos/xxx.mp4
        String location = "file:" + basePath + "/";
        registry.addResourceHandler("/static/**")
                .addResourceLocations(location);
    }
}



