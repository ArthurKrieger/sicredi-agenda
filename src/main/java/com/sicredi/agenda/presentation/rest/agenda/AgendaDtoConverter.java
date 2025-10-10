package com.sicredi.agenda.presentation.rest.agenda;

import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.domain.session.Sessions;
import com.sicredi.agenda.presentation.rest.session.SessionDto;
import com.sicredi.agenda.presentation.rest.session.SessionDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AgendaDtoConverter implements Converter<Agenda, AgendaDto> {

    private final SessionDtoConverter sessionDtoConverter;

    @Override
    public AgendaDto convert(Agenda agenda) {
        return AgendaDto.builder()
                .id(agenda.getId().map(AgendaId::id).orElse(null))
                .description(agenda.getDescription())
                .sessions(convertSessions(agenda.getSessions()))
                .build();
    }

    private List<SessionDto> convertSessions(Sessions sessions) {
        return sessions.getSessions().stream().map(sessionDtoConverter::convert).toList();
    }
}
