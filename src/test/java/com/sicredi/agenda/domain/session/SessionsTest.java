package com.sicredi.agenda.domain.session;

import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.domain.exception.SessionNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class SessionsTest {

    @Nested
    class OfEmpty {

        @Test
        void returnsEmptySessionsInstance() {
            final Sessions sessions = Sessions.ofEmpty();

            assertThat(sessions.getSessions()).isEmpty();
        }
    }

    @Nested
    class Find {

        @Test
        void returnsSessionWhenExists() {
            final Session session = TestFixtures.SESSION;
            final Sessions sessions = Sessions.builder()
                    .sessions(List.of(session))
                    .build();

            final Session result = sessions.find(TestFixtures.SESSION_ID);

            assertThat(result).isEqualTo(session);
        }

        @Test
        void throwsSessionNotFoundExceptionWhenSessionDoesNotExist() {
            final Sessions sessions = Sessions.ofEmpty();

            assertThatExceptionOfType(SessionNotFoundException.class)
                    .isThrownBy(() -> sessions.find(TestFixtures.SESSION_ID));
        }
    }

    @Nested
    class AddVote {

        @Test
        void addsVoteToExistingSession() {
            final Session originalSession = TestFixtures.SESSION;
            final Sessions sessions = Sessions.builder()
                    .sessions(List.of(originalSession))
                    .build();

            final Sessions result = sessions.addVote(TestFixtures.SESSION_ID, TestFixtures.VOTE);

            assertThat(result.getSessions()).hasSize(1);
            assertThat(result.getSessions().get(0).getSessionVotes()).containsExactly(TestFixtures.VOTE);
        }

        @Test
        void throwsSessionNotFoundExceptionWhenSessionDoesNotExist() {
            final Sessions sessions = Sessions.ofEmpty();

            assertThatExceptionOfType(SessionNotFoundException.class)
                    .isThrownBy(() -> sessions.addVote(TestFixtures.SESSION_ID, TestFixtures.VOTE));
        }
    }

    @Nested
    class Add {

        @Test
        void addsNewSessionToEmptySessions() {
            final Sessions sessions = Sessions.ofEmpty();

            final Sessions result = sessions.add(TestFixtures.SESSION);

            assertThat(result.getSessions()).hasSize(1);
            assertThat(result.getSessions()).containsExactly(TestFixtures.SESSION);
        }

        @Test
        void addsNewSessionToExistingSessions() {
            final Session existingSession = TestFixtures.SESSION.toBuilder()
                    .id(SessionId.of("existing-id"))
                    .build();
            final Sessions sessions = Sessions.builder()
                    .sessions(List.of(existingSession))
                    .build();

            final Sessions result = sessions.add(TestFixtures.SESSION);

            assertThat(result.getSessions()).hasSize(2);
            assertThat(result.getSessions()).containsExactly(existingSession, TestFixtures.SESSION);
        }
    }
}
