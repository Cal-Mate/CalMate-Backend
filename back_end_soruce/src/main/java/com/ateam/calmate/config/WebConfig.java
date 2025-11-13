package com.ateam.calmate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${profile.dirPath}")
    private String profileDirPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseDir = System.getProperty("user.dir");
        Path currentDir = Paths.get(baseDir).toAbsolutePath().normalize();
        boolean runningFromModule = currentDir.getFileName().toString().equals("back_end_soruce");
        Path backendDir = runningFromModule ? currentDir : currentDir.resolve("back_end_soruce");
        Path repoRootDir = runningFromModule && currentDir.getParent() != null
                ? currentDir.getParent()
                : currentDir;

        // 프로필 단건 이미지
        registry.addResourceHandler("/img/single/**")
                .addResourceLocations("file:" + backendDir.toString() + "/img/single/");

        // 프로필 업로드 경로 (ex: /uploads/profile/)
        registry.addResourceHandler(profileDirPath + "**")
                .addResourceLocations("file:" + backendDir.toString() + profileDirPath);

        registry.addResourceHandler("/img/meal/**")
                .addResourceLocations("file:" + backendDir.toString() + "/img/meal/");

        registry.addResourceHandler("/img/exercise/**")
                .addResourceLocations("file:" + backendDir.toString() + "/img/exercise/");

        registry.addResourceHandler("/img/diary/**")
                .addResourceLocations("file:" + backendDir.toString() + "/img/diary/");

        // (게시판 이미지, 식단 업로드 이미지, exercise 등)
        registry.addResourceHandler("/img/community/**")
                .addResourceLocations("file:" + backendDir.toString() + "/img/community/");

        // Bingo 및 일반 업로드 파일을 모두 /uploads/** 로 노출
        String bingoUploadDir = backendDir.resolve("img/event/").toUri().toString();
        String legacyUploadsDir = repoRootDir.resolve("uploads/").toUri().toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(bingoUploadDir, legacyUploadsDir);

        registry.addResourceHandler("/img/report/**")
                .addResourceLocations("file:" + backendDir.toString() + "/img/report/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 프론트 주소 확정되면 여기서 제한하면 됨
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }
}
