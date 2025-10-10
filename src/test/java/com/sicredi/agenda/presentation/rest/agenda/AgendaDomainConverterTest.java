package com.sicredi.agenda.presentation.rest.agenda;

import com.sicredi.agenda.domain.agenda.Agenda;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AgendaDomainConverterTest {

    private final AgendaDomainConverter converter = new AgendaDomainConverter();

    @Nested
    class Convert {

        @Test
        void shouldConvertAgendaDtoToDomain() {
            final AgendaDto dto = AgendaDto.builder()
                    .description("Budget approval")
                    .build();

            final Agenda result = converter.convert(dto);

            assertThat(result)
                    .isNotNull()
                    .extracting(Agenda::getDescription)
                    .isEqualTo("Budget approval");
        }
    }
}
