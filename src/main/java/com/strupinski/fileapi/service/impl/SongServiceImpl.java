package com.strupinski.fileapi.service.impl;

import com.strupinski.fileapi.model.DownloadedFile;
import com.strupinski.fileapi.repository.ResourceRepository;
import com.strupinski.fileapi.service.LocalStorageService;
import com.strupinski.fileapi.service.S3Service;
import com.strupinski.fileapi.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


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
            log.info("File uploaded in S3 storage");
            return uploadedSong.getId();
        } catch (Exception e) {
            var uploadedSong = localStorageService.upload(file);
            repository.save(uploadedSong);
            log.info("File uploaded locally");
            return uploadedSong.getId();
        }
    }

    @Override
    public DownloadedFile downloadSong(Long id) {
        try {
            var song = repository.findById(id).orElseThrow(() -> new RuntimeException("There is no file with this id"));
            if (song.getStorageType().equals("S3")) {
                log.info("File will be downloaded from s3");
                var downloadedFile = s3Service.download(song.getFilePath());
                downloadedFile.setName(song.getName());
                return downloadedFile;
            } else {
                log.info("File will be downloaded from localstorage");
                var downloadedFile = localStorageService.download(song);
                downloadedFile.setName(song.getName());
                return downloadedFile;
            }
        } catch (Exception e) {
            log.info("Error while uploading a file");
            throw new RuntimeException("Error while downloading a file");
        }
    }
}
