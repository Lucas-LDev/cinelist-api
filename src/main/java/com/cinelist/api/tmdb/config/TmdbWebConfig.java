package com.cinelist.api.tmdb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class TmdbWebConfig {

    @Value("${tmdb.api.base-url}")
    private String baseUrl;

    @Value("${tmdb.api.token}")
    private String token;

    @Bean
    public WebClient tmdbWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
