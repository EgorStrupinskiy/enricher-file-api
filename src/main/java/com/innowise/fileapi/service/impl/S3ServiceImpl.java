package com.innowise.fileapi.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.innowise.fileapi.service.S3Service;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 amazonS3;
    private String s3BucketName;

    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
        this.s3BucketName = "song-bucket";
        initializeBucket();
    }

    private void initializeBucket() {
        if (!amazonS3.doesBucketExistV2(s3BucketName)) {
            amazonS3.createBucket(s3BucketName);
        }
    }

    @Override
    public void uploadFile(MultipartFile file) {
        UUID key = UUID.randomUUID();
        try {
            amazonS3.putObject(s3BucketName, String.valueOf(key), file.getInputStream(), extractObjectMetadata(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ObjectMetadata extractObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        objectMetadata.getUserMetadata().put("fileExtension", FilenameUtils.getExtension(file.getOriginalFilename()));

        return objectMetadata;
    }
}
