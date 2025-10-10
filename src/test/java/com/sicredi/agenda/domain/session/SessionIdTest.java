package com.sicredi.agenda.domain.session;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SessionIdTest {

    @Nested
    class NewId {

        @Test
        void generatesUniqueId() {
            final SessionId sessionId1 = SessionId.newId();
            final SessionId sessionId2 = SessionId.newId();

            assertThat(sessionId1.id()).isNotNull();
            assertThat(sessionId2.id()).isNotNull();
            assertThat(sessionId1.id()).isNotEqualTo(sessionId2.id());
        }
    }

    @Nested
    class Of {

        @Test
        void createsSessionIdWithGivenId() {
            final String id = "test-id";
            final SessionId sessionId = SessionId.of(id);

            assertThat(sessionId.id()).isEqualTo(id);
        }
    }
}
