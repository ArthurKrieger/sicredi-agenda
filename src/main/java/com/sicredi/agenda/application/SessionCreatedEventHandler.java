package com.sicredi.agenda.application;

import com.sicredi.agenda.domain.session.SessionCreatedEvent;
import com.sicredi.agenda.domain.session.SessionEndedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionCreatedEventHandler {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TaskScheduler taskScheduler;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSessionCreated(SessionCreatedEvent event) {
        taskScheduler.schedule(() -> applicationEventPublisher.publishEvent(new SessionEndedEvent(event.session())), event.session().getEndTime());
    }
}
