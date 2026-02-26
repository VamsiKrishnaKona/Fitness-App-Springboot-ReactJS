package com.fitness.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig
{
    @Value("${services.user-service.base-url:http://localhost:21001}")
    private String userServiceBaseUrl;

    @Bean
    public WebClient.Builder webClientBuilder()
    {
        return WebClient.builder();
    }

    @Bean
    public WebClient userServiceWebClient(WebClient.Builder builder)
    {
        return builder
                .baseUrl(userServiceBaseUrl)
                .build();
    }
}
