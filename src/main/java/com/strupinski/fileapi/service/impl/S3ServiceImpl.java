package com.strupinski.fileapi.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.strupinski.fileapi.entity.Song;
import com.strupinski.fileapi.model.DownloadedFile;
import com.strupinski.fileapi.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class S3ServiceImpl implements S3Service {

    private final String S3 = "S3";

    private final AmazonS3 amazonS3;
    private final String s3BucketName;

    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
        this.s3BucketName = "song-bucket";
        initializeBucket();
    }

    private void initializeBucket() {
        log.info("Checking for a bucket existence");
        if (!amazonS3.doesBucketExistV2(s3BucketName)) {
            log.info("There were no bucket, creating one");
            amazonS3.createBucket(s3BucketName);
        }
    }

    @Override
    public Song upload(MultipartFile file) {
        UUID key = UUID.randomUUID();
        try {
            amazonS3.putObject(s3BucketName, String.valueOf(key), file.getInputStream(), extractObjectMetadata(file));
            return new Song(file.getOriginalFilename(), S3, String.valueOf(key));
        } catch (IOException e) {
            log.error("Error while uploading file to S3");
            throw new RuntimeException(e);
        }
    }

    @Override
    public DownloadedFile download(String id) {
        S3Object s3Object = amazonS3.getObject(s3BucketName, id);
        String filename = id + "." + s3Object.getObjectMetadata().getUserMetadata().get("fileExtension");
        Long contentLength = s3Object.getObjectMetadata().getContentLength();

        return DownloadedFile.builder().id(String.valueOf(id)).key(filename).contentLength(contentLength).inputStream(s3Object.getObjectContent())
                .build();
    }

    private ObjectMetadata extractObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        objectMetadata.getUserMetadata().put("fileExtension", FilenameUtils.getExtension(file.getOriginalFilename()));

        return objectMetadata;
    }
}
