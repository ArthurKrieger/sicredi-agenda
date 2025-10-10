package com.sicredi.agenda.application;

import com.sicredi.agenda.domain.agenda.AgendaEventRepository;
import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.SessionEndedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SessionEndedEventHandlerTest {

    @Mock
    private AgendaEventRepository agendaEventRepository;

    @InjectMocks
    private SessionEndedEventHandler sessionEndedEventHandler;

    @Test
    void sessionEndedEventIsHandledAndPublished() {
        final Session session = mock(Session.class);
        final SessionEndedEvent event = new SessionEndedEvent(session);

        sessionEndedEventHandler.handleSessionEnded(event);

        verify(agendaEventRepository).publishSessionEndedEvent(session);
    }
}