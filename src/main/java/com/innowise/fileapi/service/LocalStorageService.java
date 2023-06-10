package com.innowise.fileapi.service;

import com.innowise.fileapi.entity.Song;
import com.innowise.fileapi.model.DownloadedFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface LocalStorageService {
    Song upload(MultipartFile file);

    DownloadedFile download(Song song);
}
