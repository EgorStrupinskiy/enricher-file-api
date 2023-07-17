package com.strupinski.fileapi.service;

import com.strupinski.fileapi.entity.Song;
import com.strupinski.fileapi.model.DownloadedFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface LocalStorageService {
    Song upload(MultipartFile file);

    DownloadedFile download(Song song);
}
