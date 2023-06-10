package com.innowise.fileapi.service;

import com.innowise.fileapi.entity.Song;
import com.innowise.fileapi.model.DownloadedFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public interface S3Service {
    Song upload(MultipartFile file);

    DownloadedFile download(String id);
}
