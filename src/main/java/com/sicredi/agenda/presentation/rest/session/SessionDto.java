package com.sicredi.agenda.presentation.rest.session;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.Duration;
import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SessionDto(
        String id,
        Instant startTime,
        Instant endTime,
        Duration remainingTime,
        long favorCount,
        long againstCount
) {
}