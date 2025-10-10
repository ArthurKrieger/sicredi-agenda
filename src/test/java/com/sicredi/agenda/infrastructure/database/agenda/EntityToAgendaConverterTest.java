package com.sicredi.agenda.infrastructure.database.agenda;

import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.infrastructure.database.session.EntityToSessionConverter;
import com.sicredi.agenda.infrastructure.database.session.SessionEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityToAgendaConverterTest {

    @Mock
    private EntityToSessionConverter sessionConverter;

    @InjectMocks
    private EntityToAgendaConverter entityToAgendaConverter;

    @Nested
    class Convert {

        @Test
        void convertsEntityWithSessionsToAgenda() {
            final SessionEntity sessionEntity = mock(SessionEntity.class);
            final Session session = mock(Session.class);
            final UUID entityId = UUID.randomUUID();
            final AgendaEntity entity = AgendaEntity.builder()
                    .id(entityId)
                    .description("Test Description")
                    .sessions(List.of(sessionEntity))
                    .build();

            when(sessionConverter.convert(sessionEntity)).thenReturn(session);

            final Agenda result = entityToAgendaConverter.convert(entity);

            assertThat(result.getId()).isPresent();
            assertThat(result.getId().get()).isEqualTo(AgendaId.of(entityId.toString()));
            assertThat(result.getDescription()).isEqualTo("Test Description");
            assertThat(result.getSessions().getSessions()).containsExactly(session);
            verify(sessionConverter).convert(sessionEntity);
        }

        @Test
        void convertsEntityWithEmptySessionsToAgenda() {
            final UUID entityId = UUID.randomUUID();
            final AgendaEntity entity = AgendaEntity.builder()
                    .id(entityId)
                    .description("Test Description")
                    .sessions(List.of())
                    .build();

            final Agenda result = entityToAgendaConverter.convert(entity);

            assertThat(result.getId()).isPresent();
            assertThat(result.getId().get()).isEqualTo(AgendaId.of(entityId.toString()));
            assertThat(result.getDescription()).isEqualTo("Test Description");
            assertThat(result.getSessions().getSessions()).isEmpty();
        }
    }
}