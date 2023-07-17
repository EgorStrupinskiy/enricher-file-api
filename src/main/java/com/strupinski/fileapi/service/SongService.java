package com.strupinski.fileapi.service;

import com.strupinski.fileapi.model.DownloadedFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface SongService {
    Long addSong(MultipartFile file);

    DownloadedFile downloadSong(Long id);
}
