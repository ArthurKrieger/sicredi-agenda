package com.sicredi.agenda.infrastructure.database.agenda;

import com.sicredi.agenda.TestConversionConfig;
import com.sicredi.agenda.infrastructure.database.TestDatabaseConfig;
import com.sicredi.agenda.infrastructure.database.session.SessionEntity;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import({TestDatabaseConfig.class, TestConversionConfig.class})
@MockitoBean(types = SnsTemplate.class)
class AgendaJdbcRepositoryTest {

    @Autowired
    private AgendaJdbcRepository agendaJdbcRepository;

    @Nested
    class FindById {

        @Test
        void returnsEmptyForNonExistentAgenda() {
            final UUID nonExistentId = UUID.randomUUID();

            assertThat(agendaJdbcRepository.findById(nonExistentId)).isEmpty();
        }

        @Test
        void findsAgendaWithSessions() {
            final SessionEntity session = SessionEntity.builder()
                    .duration(Duration.ofMinutes(30))
                    .startTime(Instant.parse("2023-01-01T10:00:00Z"))
                    .votes(List.of())
                    .build();

            final AgendaEntity agenda = AgendaEntity.builder()
                    .description("Agenda with Session")
                    .sessions(List.of(session))
                    .build();

            final AgendaEntity savedAgenda = agendaJdbcRepository.save(agenda);

            assertThat(agendaJdbcRepository.findById(savedAgenda.id()))
                    .hasValueSatisfying(foundAgenda -> {
                        assertThat(foundAgenda.description()).isEqualTo("Agenda with Session");
                        assertThat(foundAgenda.sessions()).hasSize(1);
                        assertThat(foundAgenda.sessions()).anySatisfy(foundSession ->
                                assertThat(foundSession.duration()).isEqualTo(Duration.ofMinutes(30))
                        );
                    });
        }
    }
}