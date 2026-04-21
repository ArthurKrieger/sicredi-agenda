package com.sicredi.agenda.infrastructure.database.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.agenda.TestClockConfig;
import com.sicredi.agenda.TestConversionConfig;
import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.infrastructure.database.TestDatabaseConfig;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import({TestDatabaseConfig.class, TestConversionConfig.class, TestClockConfig.class})
@MockitoBean(types = SnsTemplate.class)
class JdbcBasedAgendaEventRepositoryTest {

    @Autowired
    private EventJdbcRepository eventJdbcRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcBasedAgendaEventRepository repository;

    @Autowired
    private Clock clock;


    @Nested
    class PublishSessionEndedEvent {

        @Test
        void publishesEventWithSerializedSession() throws Exception {
            final Session session = TestFixtures.SESSION;
            final Instant expectedTime = clock.instant();
            final String expectedPayload = objectMapper.writeValueAsString(new SessionEndedPayload(
                    session.getId().id(),
                    expectedTime,
                    0L,
                    0L
            ));

            repository.publishSessionEndedEvent(session);

            assertThat(eventJdbcRepository.findAll()).singleElement()
                    .satisfies(savedEvent -> {
                        assertThat(savedEvent.payload()).isEqualTo(expectedPayload);
                        assertThat(savedEvent.occurredOn()).isEqualTo(expectedTime);
                        assertThat(savedEvent.published()).isFalse();
                    });
        }
    }
}