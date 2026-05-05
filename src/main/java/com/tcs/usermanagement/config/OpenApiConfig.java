package com.tcs.usermanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userManagementOpenApi() {
        return new OpenAPI().info(new Info()
                .title("User Management Demo API")
                .description(
                        "Production-like Spring Boot demo with intentionally insecure endpoints and technical debt.")
                .version("v1")
                .contact(new Contact().name("TCS Demo Team").email("demo@example.com"))
                .license(new License().name("Demo Only").url("https://example.com/demo-only")));
    }
}