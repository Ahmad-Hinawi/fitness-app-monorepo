package com.fitness.aiservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GeminiApiConfig {
    @Bean
    public WebClient webClient() {
        return  WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
              /*  .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-goog-api-key","AIzaSyAyVvN8-Ik0EzXZmvMSpxsOnos0dI907B8")
                .build();*/

    }
}
