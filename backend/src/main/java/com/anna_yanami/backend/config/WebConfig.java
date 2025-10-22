package com.anna_yanami.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有路径应用这个CORS配置
                .allowedOrigins("http://localhost:8080") // 允许的前端源地址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 明确列出所有允许的方法
                .allowedHeaders("*") // 允许所有请求头
                .allowCredentials(true) // 允许携带凭证 (Cookie)
                .maxAge(3600); // 预检请求的有效期，单位秒
    }
}