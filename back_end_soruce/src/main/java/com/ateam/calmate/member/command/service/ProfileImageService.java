package com.ateam.calmate.member.command.service;

import com.ateam.calmate.member.command.dto.ResponseProfileImageDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class ProfileImageService {

    private String uploadDir = "";
    private String dirPath = "";

    public ProfileImageService(@Value("${profile.dirPath}") String dirPath) {

        this.dirPath = dirPath;
        uploadDir = System.getProperty("user.dir").replace("\\","/") + dirPath;
    }

    public ResponseProfileImageDTO updateProfileImage(MultipartFile singleFile, Long id, HttpServletRequest req) throws IOException {
        String originFileName = singleFile.getOriginalFilename();
        String ext = originFileName.substring(originFileName.lastIndexOf("."));
        String saveName = id + ext;
        String savePath = uploadDir + saveName;
        // 폴더가 경로상에 없을 경우 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        singleFile.transferTo(new File(savePath));

        String scheme = req.getScheme();         // http / https
        String serverName = req.getServerName(); // localhost
        int port = req.getServerPort();          // 8000
        String requestPath = scheme + "://" + serverName +  ":" + port;
        String urlImagePath = requestPath + dirPath +  saveName;

        ResponseProfileImageDTO responseProfileImageDTO = new ResponseProfileImageDTO(id, uploadDir, savePath, urlImagePath, true,"");

        return responseProfileImageDTO;
    }
}
