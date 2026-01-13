package com.mts.socialvibe_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String imagesPath = "file:" + uploadDir + "images/";
        String docsPath = "file:" + uploadDir + "docs/";

        registry.addResourceHandler("/images/**")
                .addResourceLocations(imagesPath);

        registry.addResourceHandler("/docs/**")
                .addResourceLocations(docsPath);
    }
}
