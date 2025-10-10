package com.sicredi.agenda;

import com.mercateo.test.clock.TestClock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@TestConfiguration
public class TestClockConfig {

    private static final Instant TWELVE = Instant.parse("2023-01-01T12:00:00Z");

    @Bean
    @Primary
    public Clock clock() {
        return TestClock.fixed(TWELVE, ZoneId.systemDefault());
    }

}
