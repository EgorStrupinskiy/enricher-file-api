package com.innowise.fileapi.model;

import lombok.Builder;
import lombok.Data;

import java.io.InputStream;

@Data
@Builder
public class DownloadedFile {
    private String id;
    private String key;
    private String fileName;
    private Long contentLength;
    private InputStream inputStream;
}
