package com.ateam.calmate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${profile.dirPath}")
    private String profileDirPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseDir = System.getProperty("user.dir");

        // 프로필 단건 이미지
        registry.addResourceHandler("/img/single/**")
                .addResourceLocations("file:" + baseDir + "/img/single/");

        // 프로필 업로드 경로 (ex: /uploads/profile/)
        registry.addResourceHandler(profileDirPath + "**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + profileDirPath);

        registry.addResourceHandler("/img/meal/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/img/meal/");

        registry.addResourceHandler("/img/exercise/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/img/exercise/");
    }


        // 운동 기본 이미지
        registry.addResourceHandler("/img/exercise/**")
                .addResourceLocations("file:" + baseDir + "/img/exercise/");

        // 공통 업로드 파일
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + baseDir + "/uploads/");

        // (필요하면) 식단 업로드 개별 매핑
        registry.addResourceHandler("/uploads/meal/**")
                .addResourceLocations("file:" + baseDir + "/uploads/meal/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 프론트 주소 확정되면 여기서 제한하면 됨
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }
}
