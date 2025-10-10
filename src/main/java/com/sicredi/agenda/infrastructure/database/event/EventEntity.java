package com.sicredi.agenda.infrastructure.database.event;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Builder
@Table("event")
public record EventEntity(
        @Id UUID id,
        Instant occurredOn,
        String payload,
        boolean published
) {

    public EventEntity {
        occurredOn = Objects.requireNonNullElse(occurredOn, Instant.now());
    }
}