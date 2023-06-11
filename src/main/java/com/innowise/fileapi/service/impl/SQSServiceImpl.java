package com.innowise.fileapi.service.impl;

import com.innowise.fileapi.service.SQSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SQSServiceImpl implements SQSService {

    private static final String queueName = "my-queue";
    private final SqsClient sqsClient;

    @Override
    public void addIdInQueue(Long id) {
        GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build();
        GetQueueUrlResponse getQueueUrlResponse = sqsClient.getQueueUrl(getQueueUrlRequest);
        String queueUrl = getQueueUrlResponse.queueUrl();
        if (queueUrl == null) {
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                    .queueName(queueName)
                    .build();
            CreateQueueResponse createQueueResponse = sqsClient.createQueue(createQueueRequest);
            queueUrl = createQueueResponse.queueUrl();
            log.info("New queue created: " + queueUrl);
        } else {
            log.info("Queue already exists: " + queueUrl);
        }

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(id.toString())
                .build();

        sqsClient.sendMessage(sendMessageRequest);
        log.info("Message was uploaded in queue");
    }
}
