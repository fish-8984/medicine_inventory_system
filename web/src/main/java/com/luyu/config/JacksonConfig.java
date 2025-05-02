package com.luyu.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luyu.json.JacksonObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new JacksonObjectMapper();
    }
}
