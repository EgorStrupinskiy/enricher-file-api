package com.innowise.fileapi.service.impl;

import com.innowise.fileapi.repository.ResourceRepository;
import com.innowise.fileapi.service.LocalStorageService;
import com.innowise.fileapi.service.S3Service;
import com.innowise.fileapi.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final ResourceRepository repository;
    private final S3Service s3Service;
    private final LocalStorageService localStorageService;

    @Override
    public ResponseEntity<String> addSong(MultipartFile file) {
        try {
            s3Service.uploadFile(file);
            return ResponseEntity.ok("S3");
        } catch (Exception e) {
            localStorageService.uploadFile(file);
            return ResponseEntity.ok("Locally");
        }
    }
}
