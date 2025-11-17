package com.example.quizzer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Quizzer API")
                        .version("v1")
                        .description("REST API for Quizzer application")
                        .contact(new Contact().name("Quiz Team").email("devs@example.com"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }
}
