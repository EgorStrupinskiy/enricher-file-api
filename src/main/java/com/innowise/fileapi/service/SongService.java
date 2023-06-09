package com.innowise.fileapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SongService {
    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${upload.path}")
    private String folderPath;

    @Autowired
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    public ResponseEntity<String> processFile(MultipartFile file) throws IOException {
        try {
            return saveInCloud(file);
        } catch (Exception e) {
            return saveFileLocally(file);
        }
    }

    public ResponseEntity<String> saveInCloud(MultipartFile file) {
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileExtension = StringUtils.getFilenameExtension(fileName);
            String storedFileName = UUID.randomUUID().toString() + "." + fileExtension;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("my-bucket")
                    .key(storedFileName)
                    .build();

            byte[] fileBytes = file.getBytes();
            InputStream inputStream = new ByteArrayInputStream(fileBytes);
            s3Client().putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, fileBytes.length));

            String fileUrl = "https://" + "my-bucket" + ".s3.amazonaws.com/" + storedFileName;
            return ResponseEntity.ok("File uploaded successfully, Path: " + fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<String> saveFileLocally(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String filePath = folderPath + File.separator + fileName;

            String projectRoot = System.getProperty("user.dir");
            String absolutePath = projectRoot + File.separator + filePath;

            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            file.transferTo(new File(absolutePath));

            File savedFile = new File(absolutePath);
            if (savedFile.exists()) {
                return ResponseEntity.ok("File saved successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save file");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
