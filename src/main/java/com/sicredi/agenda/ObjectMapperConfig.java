package com.sicredi.agenda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;

public class ObjectMapperConfig {

    public static final ObjectMapper INSTANCE;

    static {
        INSTANCE = new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    @Bean
    public static ObjectMapper objectMapper() {
        return INSTANCE;
    }
}
