package com.innowise.fileapi.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface LocalStorageService {
    String uploadFile(MultipartFile file);
}
