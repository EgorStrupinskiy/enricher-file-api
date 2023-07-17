package com.strupinski.fileapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class SQSClientConfiguration {

    @Value("${s3.endpoint}")
    private String sqsEndpointUrl;
    @Value("${s3.region}")
    private String sqsRegion;

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .credentialsProvider(() -> AwsBasicCredentials.create("test", "test"))
                .region(Region.of(sqsRegion))
                .endpointOverride(URI.create(sqsEndpointUrl))
                .build();
    }
}
