package com.sicredi.agenda.domain.exception;

import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaId;

public class AgendaNotFoundException extends ResourceNotFoundException {
    public AgendaNotFoundException(AgendaId agendaId) {
        super(agendaId.id());
    }
}
