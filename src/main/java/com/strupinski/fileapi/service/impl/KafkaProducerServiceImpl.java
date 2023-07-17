package com.strupinski.fileapi.service.impl;

import com.strupinski.fileapi.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final KafkaTemplate<Object, Object> template;

    @Override
    public HttpStatus send(Long id) {
        try {
            log.info("producing message to Kafka, topic=file-api-topic");
            this.template.send("file-api-topic", id.toString());
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.OK;
    }
}
