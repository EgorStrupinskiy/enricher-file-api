package com.innowise.fileapi.service.impl;

import com.innowise.fileapi.service.LocalStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class LocalStorageServiceImpl implements LocalStorageService {

    @Value("${upload.path}")
    private String folderPath;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String filePath = folderPath + File.separator + fileName;

            String projectRoot = System.getProperty("user.dir");
            String absolutePath = projectRoot + File.separator + filePath;
            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            file.transferTo(new File(absolutePath));
            File savedFile = new File(absolutePath);
            if (savedFile.exists()) {
                return absolutePath;
            } else {
                throw new RuntimeException("Failed to save file");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save file");
        }
    }
}
