package com.sicredi.agenda.presentation.rest.agenda;

import com.sicredi.agenda.domain.agenda.Agenda;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AgendaDomainConverter implements Converter<AgendaDto, Agenda> {

    @Override
    public Agenda convert(AgendaDto agendaDto) {
        return Agenda.builder()
                .description(agendaDto.description())
                .build();
    }
}
