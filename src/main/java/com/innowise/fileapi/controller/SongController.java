package com.innowise.fileapi.controller;

import com.innowise.fileapi.service.SQSService;
import com.innowise.fileapi.service.SongService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class SongController {
    private final SongService songService;
    private final SQSService sqsService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestBody MultipartFile file) {
        try {
            sqsService.addIdInQueue(songService.addSong(file));
            log.info("Song id uploaded in queue");
            return ResponseEntity.ok().body("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) {
        try {
            var downloadedFile = songService.downloadSong(id);
            log.info("File was downloaded successfully");
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadedFile.getFileName())
                    .contentLength(downloadedFile.getContentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(downloadedFile.getInputStream()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
