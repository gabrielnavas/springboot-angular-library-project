package com.library.api.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("This is an API Library Project")
                                .version("1.0.0")
                                .description("RESTful API Library Project with Java 17 and Spring Boot 3")
                                .termsOfService("https://gabrielnavas.github.io/")
                                .license(new License().name("Apache 2.0"))
                )
                ;
    }
}
