package com.sicredi.agenda.infrastructure.database;

import com.sicredi.agenda.ObjectMapperConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@TestConfiguration
@Import({JdbcConfig.class, ObjectMapperConfig.class})
public class TestDatabaseConfig {

    @Bean
    public Clock clock() {
        return Clock.fixed(Instant.parse("2023-01-01T10:02:00Z"), ZoneOffset.UTC);
    }
}
