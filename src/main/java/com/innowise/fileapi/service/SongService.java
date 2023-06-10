package com.innowise.fileapi.service;

import com.innowise.fileapi.model.DownloadedFile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface SongService {
    ResponseEntity<String> addSong(MultipartFile file);

    DownloadedFile downloadSong(Long id);
}
