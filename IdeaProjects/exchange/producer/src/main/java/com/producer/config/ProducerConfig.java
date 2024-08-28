package com.producer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ProducerConfig {

    @Value("${base.url}")
    private String url;

    @Bean
    RestClient getRestClient() {
        return RestClient.create(url);
    }
}
