package com.sicredi.agenda.infrastructure.database.agenda;


import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.infrastructure.database.session.SessionEntity;
import com.sicredi.agenda.infrastructure.database.session.SessionToEntityConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AgendaToEntityConverter implements Converter<Agenda, AgendaEntity> {

    private final SessionToEntityConverter sessionConverter;

    @Override
    public AgendaEntity convert(Agenda agenda) {
        final List<SessionEntity> sessionEntities = agenda.getSessions().getSessions().stream()
                .map(sessionConverter::convert)
                .toList();
        return AgendaEntity.builder()
                .id(agenda.getId().map(AgendaId::id).map(UUID::fromString).orElse(null))
                .description(agenda.getDescription())
                .sessions(sessionEntities)
                .build();
    }
}