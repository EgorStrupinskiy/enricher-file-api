package com.strupinski.fileapi.service.impl;

import com.strupinski.fileapi.entity.Song;
import com.strupinski.fileapi.model.DownloadedFile;
import com.strupinski.fileapi.service.LocalStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class LocalStorageServiceImpl implements LocalStorageService {

    private final String LOCALLY = "locally";
    @Value("${upload.path}")
    private String folderPath;

    @Override
    public Song upload(MultipartFile file) {
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
                return new Song(fileName, LOCALLY, absolutePath);
            } else {
                throw new RuntimeException("Failed to save file");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save file");
        }
    }

    @Override
    public DownloadedFile download(Song song) {
        File file = new File(song.getFilePath());
        if (file.exists()) {
            Long contentLength = file.length();
            try {
                InputStream inputStream = new FileInputStream(file);
                return DownloadedFile.builder()
                        .id(song.getFilePath())
                        .key(song.getName())
                        .name(song.getName())
                        .contentLength(contentLength)
                        .inputStream(inputStream)
                        .build();
            } catch (IOException e) {
                throw new RuntimeException("Error while downloading file from localstorage");
            }
        } else {
            throw new RuntimeException("There is no such file in localstorage");
        }
    }
}
