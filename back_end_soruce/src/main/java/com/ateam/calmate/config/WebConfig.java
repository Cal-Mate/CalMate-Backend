package com.ateam.calmate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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



    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                  // origin 이후 요청 경로 패턴
                .allowedOrigins("*")        // origin 등록(어떤 front 서버든 상관 없이 허용)
                // .allowedOrigins("http://localhost:5173")        // origin 등록
                // .allowedOrigins("http://localhost:8011")        // origin 등록(프론트가 컨테이너화 된 이후)
//                .allowedOrigins("http://localhost:30000")        // origin 등록(k8s nodeport 이후)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

}