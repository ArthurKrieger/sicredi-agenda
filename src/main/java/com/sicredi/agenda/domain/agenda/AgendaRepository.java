package com.sicredi.agenda.domain.agenda;

import java.util.Optional;

public interface AgendaRepository {

    Agenda saveAgenda(final Agenda agenda);

    Optional<Agenda> findAgenda(final AgendaId agendaId);
}
