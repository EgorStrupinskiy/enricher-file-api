package com.innowise.fileapi.service.impl;

import com.innowise.fileapi.model.DownloadedFile;
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
            var uploadedSong = s3Service.upload(file);
            repository.save(uploadedSong);
            return ResponseEntity.ok("File saved in S3, id = " + uploadedSong.getId());
        } catch (Exception e) {
            var uploadedSong = localStorageService.upload(file);
            repository.save(uploadedSong);
            return ResponseEntity.ok("File saved locally, id = " + uploadedSong.getId());
        }
    }

    @Override
    public DownloadedFile downloadSong(Long id) {
        try {
            var song = repository.findById(id).orElseThrow(() -> new RuntimeException("There is no file with this id"));
            if (song.getStorageType().equals("S3")) {
                return s3Service.download(song.getFilePath());
            } else {
                return localStorageService.download(song);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while uploading a file");
        }
    }
}
