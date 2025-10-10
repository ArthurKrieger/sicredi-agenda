package com.sicredi.agenda.infrastructure.database.jdbc;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ReadingConverter
public class LongToDurationConverter implements Converter<Long, Duration> {
    @Override
    public Duration convert(Long source) {
        return Duration.ofSeconds(source);
    }
}