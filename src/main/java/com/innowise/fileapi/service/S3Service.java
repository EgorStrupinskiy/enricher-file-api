package com.innowise.fileapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface S3Service {
    void uploadFile(MultipartFile file);
}
