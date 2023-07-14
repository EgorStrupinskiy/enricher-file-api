package com.innowise.fileapi.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public interface KafkaProducerService {
    HttpStatus send(Long id);
}
