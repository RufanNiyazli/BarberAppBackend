package com.project.barberreservation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.dir.profile}")
    private String profileDir;

    @Value("${upload.dir.gallery}")
    private String galleryDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/profile/**")
                .addResourceLocations("file:" + profileDir + "/");
        registry.addResourceHandler("/files/gallery/**")
                .addResourceLocations("file:" + galleryDir + "/");
    }
}
