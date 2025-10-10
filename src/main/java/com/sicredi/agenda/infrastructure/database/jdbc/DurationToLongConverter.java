package com.sicredi.agenda.infrastructure.database.jdbc;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@WritingConverter
public class DurationToLongConverter implements Converter<Duration, Long> {
    @Override
    public Long convert(Duration source) {
        return source.getSeconds();
    }
}