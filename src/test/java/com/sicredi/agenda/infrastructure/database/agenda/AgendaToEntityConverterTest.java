package com.sicredi.agenda.infrastructure.database.agenda;

import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.session.Sessions;
import com.sicredi.agenda.infrastructure.database.session.SessionEntity;
import com.sicredi.agenda.infrastructure.database.session.SessionToEntityConverter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendaToEntityConverterTest {

    @Mock
    private SessionToEntityConverter sessionConverter;

    @InjectMocks
    private AgendaToEntityConverter agendaToEntityConverter;

    @Nested
    class Convert {

        @Test
        void convertsAgendaWithIdToEntity() {
            final SessionEntity sessionEntity = mock(SessionEntity.class);
            when(sessionConverter.convert(TestFixtures.SESSION)).thenReturn(sessionEntity);

            final AgendaEntity result = agendaToEntityConverter.convert(TestFixtures.AGENDA);

            assertThat(result.id()).isEqualTo(UUID.fromString(TestFixtures.AGENDA_ID.id()));
            assertThat(result.description()).isEqualTo(TestFixtures.AGENDA.getDescription());
            assertThat(result.sessions()).containsExactly(sessionEntity);
            verify(sessionConverter).convert(TestFixtures.SESSION);
        }

        @Test
        void convertsAgendaWithoutIdToEntity() {
            final Agenda agendaWithoutId = TestFixtures.AGENDA.toBuilder()
                    .id(null)
                    .build();

            final AgendaEntity result = agendaToEntityConverter.convert(agendaWithoutId);

            assertThat(result.id()).isNull();
            assertThat(result.description()).isEqualTo(agendaWithoutId.getDescription());
        }

        @Test
        void convertsAgendaWithEmptySessionsToEntity() {
            final Agenda agendaWithEmptySessions = TestFixtures.AGENDA.toBuilder()
                    .sessions(Sessions.ofEmpty())
                    .build();

            final AgendaEntity result = agendaToEntityConverter.convert(agendaWithEmptySessions);

            assertThat(result.id()).isEqualTo(UUID.fromString(TestFixtures.AGENDA_ID.id()));
            assertThat(result.description()).isEqualTo(agendaWithEmptySessions.getDescription());
            assertThat(result.sessions()).isEmpty();
        }
    }
}