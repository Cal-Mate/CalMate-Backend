package com.ateam.calmate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${profile.dirPath}")
    String profileDirPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/single/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/img/single/");

        registry.addResourceHandler(profileDirPath + "**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + profileDirPath);
    }

}