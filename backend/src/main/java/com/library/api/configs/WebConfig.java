package com.library.api.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.originPatterns:default}")
    private String corsOriginpatterns = "";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] allowedOrigins = corsOriginpatterns.split(",");
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins(allowedOrigins)
                .allowCredentials(true);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // via QUERY PAARAM. http://localhost:8080/recurse?media=xml
        //        configurer.favorParameter(true)
        //                .parameterName("mediaType").ignoreAcceptHeader(true)
        //                .useRegisteredExtensionsOnly(false)
        //                .defaultContentType(MediaType.APPLICATION_JSON)
        //                .mediaType("json", MediaType.APPLICATION_JSON)
        //                .mediaType("xml", MediaType.APPLICATION_XML);

        // via HEADER PAARAM. http://localhost:8080/recurse
        configurer.favorParameter(false)
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML);

    }
}
