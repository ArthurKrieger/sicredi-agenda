package com.sicredi.agenda.application;

import com.sicredi.agenda.domain.agenda.AgendaEventRepository;
import com.sicredi.agenda.domain.session.SessionEndedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionEndedEventHandler {

    private final AgendaEventRepository agendaEventRepository;

    @EventListener
    public void handleSessionEnded(SessionEndedEvent event) {
        agendaEventRepository.publishSessionEndedEvent(event.session());
    }
}
