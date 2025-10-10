package com.sicredi.agenda;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.List;

@TestConfiguration
public class TestConversionConfig {
    @Bean
    public ConversionService conversionService(List<Converter<?, ?>> converters) {
        DefaultConversionService conversionService = new DefaultConversionService();
        converters.forEach(conversionService::addConverter);
        return conversionService;
    }
}