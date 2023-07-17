package com.strupinski.fileapi.service.impl;

import com.strupinski.fileapi.service.SQSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SQSServiceImpl implements SQSService {

    private static final String queueName = "file-api-queue";
    private final SqsClient sqsClient;

    @Override
    public void addIdInQueue(Long id) {
        try {
            GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build();
            GetQueueUrlResponse getQueueUrlResponse = sqsClient.getQueueUrl(getQueueUrlRequest);
            String queueUrl = getQueueUrlResponse.queueUrl();
            sendMessageToQueue(queueUrl, id);
        } catch (QueueDoesNotExistException e) {
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                    .queueName(queueName)
                    .build();
            String queueUrl = sqsClient.createQueue(createQueueRequest).queueUrl();
            sendMessageToQueue(queueUrl, id);
        }
        log.info("Message was uploaded in queue");
    }

    private void sendMessageToQueue(String queueUrl, Long id) {
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(id.toString())
                .build();

        sqsClient.sendMessage(sendMessageRequest);
    }

}
