package com.ateam.calmate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${profile.dirPath}") // 예: /uploads/profile/
    private String profileDirPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseDir = System.getProperty("user.dir");

        // 단일 프로필 기본 이미지
        registry.addResourceHandler("/img/single/**")
                .addResourceLocations("file:" + baseDir + "/img/single/");

        // 식단 기본 이미지
        registry.addResourceHandler("/img/meal/**")
                .addResourceLocations("file:" + baseDir + "/img/meal/");

        // 운동 기본 이미지
        registry.addResourceHandler("/img/exercise/**")
                .addResourceLocations("file:" + baseDir + "/img/exercise/");

        // 프로필 업로드(실제 사용자 업로드 경로)
        // profileDirPath 예: "/uploads/profile/"
        registry.addResourceHandler(profileDirPath + "**")
                .addResourceLocations("file:" + baseDir + profileDirPath);

        // 공통 업로드 파일
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + baseDir + "/uploads/");

        // (선택) 식단 업로드 전용 경로 (프론트에서 /uploads/meal/ 쓰면 명시)
        registry.addResourceHandler("/uploads/meal/**")
                .addResourceLocations("file:" + baseDir + "/uploads/meal/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 나중에 프론트 도메인으로 제한 가능
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }
}
