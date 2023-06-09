package com.innowise.fileapi.controller;

import com.innowise.fileapi.service.impl.SongServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {
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
}
