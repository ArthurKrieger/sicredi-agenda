package com.sicredi.agenda.infrastructure.database.event;

import com.sicredi.agenda.TestConversionConfig;
import com.sicredi.agenda.infrastructure.database.TestDatabaseConfig;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import({TestDatabaseConfig.class, TestConversionConfig.class})
@MockitoBean(types = SnsTemplate.class)
class EventJdbcRepositoryTest {

    @Autowired
    private EventJdbcRepository eventJdbcRepository;

    @BeforeEach
    void setUp() {
        eventJdbcRepository.deleteAll();
    }

    @Nested
    class FindUnpublishedEvents {

        @Test
        void returnsOnlyUnpublishedEventsOrderedByOccurredOn() {
            final Instant TEN = Instant.parse("2023-01-01T10:00:00Z");
            final Instant ELEVEN = Instant.parse("2023-01-01T11:00:00Z");
            final Instant TWELVE = Instant.parse("2023-01-01T12:00:00Z");

            final EventEntity publishedEvent = EventEntity.builder()
                    .payload("published-payload")
                    .occurredOn(ELEVEN)
                    .published(true)
                    .build();

            final EventEntity unpublishedEvent1 = EventEntity.builder()
                    .payload("unpublished-payload-1")
                    .occurredOn(TWELVE)
                    .published(false)
                    .build();

            final EventEntity unpublishedEvent2 = EventEntity.builder()
                    .payload("unpublished-payload-2")
                    .occurredOn(TEN)
                    .published(false)
                    .build();

            eventJdbcRepository.save(publishedEvent);
            eventJdbcRepository.save(unpublishedEvent1);
            eventJdbcRepository.save(unpublishedEvent2);

            final List<EventEntity> result = eventJdbcRepository.findUnpublishedEvents();

            assertThat(result).hasSize(2);
            assertThat(result).anySatisfy(event -> {
                assertThat(event.payload()).isEqualTo("unpublished-payload-2");
                assertThat(event.occurredOn()).isEqualTo(TEN);
            });
            assertThat(result).anySatisfy(event -> {
                assertThat(event.payload()).isEqualTo("unpublished-payload-1");
                assertThat(event.occurredOn()).isEqualTo(TWELVE);
            });
        }

        @Test
        void returnsEmptyListWhenNoUnpublishedEvents() {
            final EventEntity publishedEvent = EventEntity.builder()
                    .payload("published-payload")
                    .occurredOn(Instant.now())
                    .published(true)
                    .build();

            eventJdbcRepository.save(publishedEvent);

            final List<EventEntity> result = eventJdbcRepository.findUnpublishedEvents();

            assertThat(result).isEmpty();
        }

        @Test
        void returnsEmptyListWhenNoEvents() {
            final List<EventEntity> result = eventJdbcRepository.findUnpublishedEvents();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    class MarkAsPublished {

        @Test
        void marksEventAsPublished() {
            final EventEntity unpublishedEvent = EventEntity.builder()
                    .payload("test-payload")
                    .occurredOn(Instant.now())
                    .published(false)
                    .build();

            final EventEntity savedEvent = eventJdbcRepository.save(unpublishedEvent);

            eventJdbcRepository.markAsPublished(savedEvent.id());

            assertThat(eventJdbcRepository.findById(savedEvent.id()))
                    .hasValueSatisfying(event -> assertThat(event.published()).isTrue());
        }

        @Test
        void doesNotAffectOtherEvents() {
            final EventEntity event1 = EventEntity.builder()
                    .payload("payload-1")
                    .occurredOn(Instant.now())
                    .published(false)
                    .build();

            final EventEntity event2 = EventEntity.builder()
                    .payload("payload-2")
                    .occurredOn(Instant.now())
                    .published(false)
                    .build();

            final EventEntity savedEvent1 = eventJdbcRepository.save(event1);
            final EventEntity savedEvent2 = eventJdbcRepository.save(event2);

            eventJdbcRepository.markAsPublished(savedEvent1.id());

            assertThat(eventJdbcRepository.findById(savedEvent1.id()))
                    .hasValueSatisfying(event -> assertThat(event.published()).isTrue());

            assertThat(eventJdbcRepository.findById(savedEvent2.id()))
                    .hasValueSatisfying(event -> assertThat(event.published()).isFalse());
        }

        @Test
        void doesNothingWhenEventNotFound() {
            final UUID nonExistentId = UUID.randomUUID();

            eventJdbcRepository.markAsPublished(nonExistentId);

            assertThat(eventJdbcRepository.findById(nonExistentId)).isEmpty();
        }
    }
}