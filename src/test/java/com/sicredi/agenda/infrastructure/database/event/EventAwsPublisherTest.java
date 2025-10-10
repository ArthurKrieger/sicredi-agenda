package com.sicredi.agenda.infrastructure.database.event;

import io.awspring.cloud.sns.core.SnsTemplate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventAwsPublisherTest {

    @Mock
    private EventJdbcRepository eventJdbcRepository;

    @Mock
    private SnsTemplate snsTemplate;

    @InjectMocks
    private EventAwsPublisher eventAwsPublisher;

    @Nested
    class PublishEvents {

        @Test
        void withEvents_publishesAndMarksAsPublished() {
            final EventEntity event1 = EventEntity.builder()
                    .id(UUID.randomUUID())
                    .payload("event1-payload")
                    .published(false)
                    .build();
            final EventEntity event2 = EventEntity.builder()
                    .id(UUID.randomUUID())
                    .payload("event2-payload")
                    .published(false)
                    .build();

            when(eventJdbcRepository.findAll()).thenReturn(List.of(event1, event2));

            eventAwsPublisher.publishEvents();

            verify(snsTemplate).convertAndSend("agenda-events", "event1-payload");
            verify(snsTemplate).convertAndSend("agenda-events", "event2-payload");
            verify(eventJdbcRepository).markAsPublished(event1.id());
            verify(eventJdbcRepository).markAsPublished(event2.id());
        }

        @Test
        void withNoEvents_noInteractions() {
            when(eventJdbcRepository.findAll()).thenReturn(List.of());

            eventAwsPublisher.publishEvents();

            verifyNoInteractions(snsTemplate);
            verify(eventJdbcRepository, never()).markAsPublished(any());
        }
    }
}