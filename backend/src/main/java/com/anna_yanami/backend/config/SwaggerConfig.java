package com.anna_yanami.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("XX项目API") // 文档标题
                        .version("1.0") // 版本号
                        .description("XX项目的 Swagger API 文档（Spring Boot 3 + Springdoc）")
                        .contact(new Contact()
                                .name("Anna_Yanami")
                                .url("https://blog.csdn.net/")
                                .email("2185086485@qq.com")));
    }
}
