package com.sicredi.agenda.infrastructure.database.agenda;

import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.domain.session.Sessions;
import com.sicredi.agenda.infrastructure.database.session.EntityToSessionConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityToAgendaConverter implements Converter<AgendaEntity, Agenda> {

    private final EntityToSessionConverter sessionConverter;

    @Override
    public Agenda convert(AgendaEntity entity) {
        final Sessions sessions = Sessions.builder()
                .sessions(entity.sessions().stream().map(sessionConverter::convert).toList())
                .build();
        return Agenda.builder()
                .id(AgendaId.of(entity.id().toString()))
                .description(entity.description())
                .sessions(sessions)
                .build();
    }
}