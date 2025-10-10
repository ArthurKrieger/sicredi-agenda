package com.sicredi.agenda.presentation.rest.agenda;

import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.presentation.rest.session.SessionDto;
import com.sicredi.agenda.presentation.rest.session.SessionDtoConverter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class AgendaDtoConverterTest {

    private final SessionDtoConverter sessionDtoConverter = Mockito.mock(SessionDtoConverter.class);
    private final AgendaDtoConverter converter = new AgendaDtoConverter(sessionDtoConverter);

    @Nested
    class Convert {

        private static Stream<Arguments> agendaIdProvider() {
            return Stream.of(Arguments.of(AgendaId.of("id")));
        }

        @ParameterizedTest
        @NullSource
        @MethodSource("agendaIdProvider")
        void shouldConvertAgendaWithDifferentIds(final AgendaId agendaId) {
            final Agenda agenda = TestFixtures.AGENDA.toBuilder()
                    .id(agendaId)
                    .description("Agenda with parameterized ID")
                    .build();

            final SessionDto sessionDto = SessionDto.builder()
                    .id(TestFixtures.SESSION_ID.id())
                    .build();

            when(sessionDtoConverter.convert(TestFixtures.SESSION)).thenReturn(sessionDto);

            final AgendaDto result = converter.convert(agenda);

            assertThat(result).isNotNull();
            assertThat(Optional.ofNullable(agendaId).map(AgendaId::id).orElse(null));
            assertThat(result.description()).isEqualTo("Agenda with parameterized ID");
            assertThat(result.sessions())
                    .hasSize(1)
                    .first()
                    .extracting(SessionDto::id)
                    .isEqualTo(TestFixtures.SESSION_ID.id());
        }
    }
}