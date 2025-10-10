package com.sicredi.agenda.domain.agenda;

import com.sicredi.agenda.domain.session.Session;

public interface AgendaEventRepository {

    void publishSessionEndedEvent(Session session);
}
