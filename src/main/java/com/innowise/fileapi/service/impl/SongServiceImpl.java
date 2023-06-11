package com.innowise.fileapi.service.impl;

import com.innowise.fileapi.model.DownloadedFile;
import com.innowise.fileapi.repository.ResourceRepository;
import com.innowise.fileapi.service.LocalStorageService;
import com.innowise.fileapi.service.S3Service;
import com.innowise.fileapi.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.sun.activation.registries.LogSupport.log;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final ResourceRepository repository;
    private final S3Service s3Service;
    private final LocalStorageService localStorageService;

    @Override
    public Long addSong(MultipartFile file) {
        try {
            var uploadedSong = s3Service.upload(file);
            repository.save(uploadedSong);
            log("File uploaded in S3 storage");
            return uploadedSong.getId();
        } catch (Exception e) {
            var uploadedSong = localStorageService.upload(file);
            repository.save(uploadedSong);
            log("File uploaded in locally");
            return uploadedSong.getId();
        }
    }

    @Override
    public DownloadedFile downloadSong(Long id) {
        try {
            var song = repository.findById(id).orElseThrow(() -> new RuntimeException("There is no file with this id"));
            if (song.getStorageType().equals("S3")) {
                log.info("File will be downloaded from s3");
                return s3Service.download(song.getFilePath());
            } else {
                log.info("File will be downloaded from localstorage");
                return localStorageService.download(song);
            }
        } catch (Exception e) {
            log.info("Error while uploading a file");
            throw new RuntimeException("Error while uploading a file");
        }
    }
}
