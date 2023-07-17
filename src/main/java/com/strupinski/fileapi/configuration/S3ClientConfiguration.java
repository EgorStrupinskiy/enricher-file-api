package com.strupinski.fileapi.configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3ClientConfiguration {

    @Value("${s3.endpoint}")
    private String endpointUrl;
    @Value("${s3.region}")
    private String region;

    @Bean
    AmazonS3 amazonS3() {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpointUrl,
                region);
        return AmazonS3ClientBuilder.standard().withEndpointConfiguration(endpointConfiguration).withPathStyleAccessEnabled(true).build();
    }
}
