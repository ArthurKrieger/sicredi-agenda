package com.sicredi.agenda.domain.agenda;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AgendaIdTest {

    @Nested
    class Of {

        @Test
        void createsAgendaIdFromString() {
            final String id = "test-id";
            final AgendaId agendaId = AgendaId.of(id);

            assertThat(agendaId.id()).isEqualTo(id);
        }

    }

}