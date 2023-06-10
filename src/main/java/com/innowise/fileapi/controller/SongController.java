package com.innowise.fileapi.controller;

import com.innowise.fileapi.service.impl.SongServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class SongController {
    private final SongServiceImpl songService;

    @GetMapping("/hello")
    public String greetingsMethod() {
        return "Hello world";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestBody MultipartFile file) {
        try {
            return songService.addSong(file);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam Long id) {
        try {
            var downloadedFile = songService.downloadSong(id);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadedFile.getFileName())
                    .contentLength(downloadedFile.getContentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(downloadedFile.getInputStream()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
