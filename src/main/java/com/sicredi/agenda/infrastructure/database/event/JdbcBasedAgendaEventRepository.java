package com.sicredi.agenda.infrastructure.database.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.agenda.domain.agenda.AgendaEventRepository;
import com.sicredi.agenda.domain.session.Session;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JdbcBasedAgendaEventRepository implements AgendaEventRepository {

    private final EventJdbcRepository eventJdbcRepository;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    @Override
    @SneakyThrows
    public void publishSessionEndedEvent(Session session) {
        final EventEntity event = EventEntity.builder()
                .payload(objectMapper.writeValueAsString(session))
                .occurredOn(Instant.now(clock))
                .build();
        eventJdbcRepository.save(event);
    }
}
