package com.sicredi.agenda.infrastructure.database.agenda;

import com.sicredi.agenda.TestConversionConfig;
import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.domain.session.Sessions;
import com.sicredi.agenda.infrastructure.database.JdbcConfig;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {JdbcConfig.class, TestConversionConfig.class})
@MockitoBean(types = SnsTemplate.class)
class JdbcBasedAgendaRepositoryTest {

    @Autowired
    private JdbcBasedAgendaRepository repository;


    @Nested
    class SaveAgenda {

        @Test
        void savesAgendaAndGeneratesId() {
            final Agenda agendaWithoutId = TestFixtures.AGENDA.toBuilder()
                    .id(null)
                    .sessions(Sessions.ofEmpty())
                    .build();

            final Agenda result = repository.saveAgenda(agendaWithoutId);

            assertThat(result.getId()).isPresent();
            assertThat(result.getDescription()).isEqualTo(agendaWithoutId.getDescription());
        }

        @Test
        void savesAgendaWithExistingId() {
            final Agenda savedAgenda = repository.saveAgenda(TestFixtures.AGENDA.toBuilder()
                    .id(null)
                    .sessions(Sessions.ofEmpty())
                    .build());

            final Agenda updatedAgenda = savedAgenda.toBuilder()
                    .description("Updated Description")
                    .build();

            final Agenda result = repository.saveAgenda(updatedAgenda);

            assertThat(result.getId()).isEqualTo(savedAgenda.getId());
            assertThat(result.getDescription()).isEqualTo("Updated Description");
        }
    }

    @Nested
    class FindAgenda {

        @Test
        void findsExistingAgenda() {
            final Agenda savedAgenda = repository.saveAgenda(TestFixtures.AGENDA.toBuilder()
                    .id(null)
                    .sessions(Sessions.ofEmpty())
                    .build());

            final Optional<Agenda> result = repository.findAgenda(savedAgenda.getId().get());

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(savedAgenda.getId());
            assertThat(result.get().getDescription()).isEqualTo(savedAgenda.getDescription());
        }

        @Test
        void returnsEmptyForNonExistentAgenda() {
            final AgendaId nonExistentId = AgendaId.of("00000000-0000-0000-0000-000000000000");

            final Optional<Agenda> result = repository.findAgenda(nonExistentId);

            assertThat(result).isEmpty();
        }
    }

}