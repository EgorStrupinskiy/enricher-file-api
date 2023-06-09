package com.innowise.fileapi.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface SongService {
    ResponseEntity<String> addSong(MultipartFile file);
}
