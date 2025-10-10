package com.sicredi.agenda.application;

import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.SessionCreatedEvent;
import com.sicredi.agenda.domain.session.SessionEndedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionCreatedEventHandlerTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private TaskScheduler taskScheduler;

    @InjectMocks
    private SessionCreatedEventHandler eventHandler;

    @Test
    void sessionCreated_schedulesTaskThatPublishesSessionEndedEvent() {
        final Session session = mock(Session.class);
        final Instant endTime = Instant.now().plusSeconds(60);
        when(session.getEndTime()).thenReturn(endTime);
        final SessionCreatedEvent event = new SessionCreatedEvent(session);
        final ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        eventHandler.handleSessionCreated(event);

        verify(taskScheduler).schedule(runnableCaptor.capture(), eq(endTime));
        runnableCaptor.getValue().run();
        verify(applicationEventPublisher).publishEvent(any(SessionEndedEvent.class));
    }
}