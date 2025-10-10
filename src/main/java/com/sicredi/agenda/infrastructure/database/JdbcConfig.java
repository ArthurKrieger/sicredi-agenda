package com.sicredi.agenda.infrastructure.database;

import com.sicredi.agenda.infrastructure.database.jdbc.DurationToLongConverter;
import com.sicredi.agenda.infrastructure.database.jdbc.LongToDurationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.List;

@Configuration
@ComponentScan
@EnableJdbcRepositories
public class JdbcConfig {

    @Bean
    public JdbcCustomConversions jdbcCustomConversions(DurationToLongConverter durationToLongConverter, LongToDurationConverter longToDurationConverter) {
        return new JdbcCustomConversions(List.of(
                durationToLongConverter,
                longToDurationConverter
        ));
    }

}