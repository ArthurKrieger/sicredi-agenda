package com.sicredi.agenda.presentation.rest.agenda;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sicredi.agenda.presentation.rest.session.SessionDto;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AgendaDto(String id, String description, List<SessionDto> sessions) {
}
