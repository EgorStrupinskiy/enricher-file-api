package com.innowise.fileapi.service;

import com.innowise.fileapi.model.DownloadedFile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public interface SongService {
    ResponseEntity<String> addSong(MultipartFile file);

    DownloadedFile downloadSong(String key);
}
