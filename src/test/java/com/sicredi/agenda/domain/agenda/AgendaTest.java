package com.sicredi.agenda.domain.agenda;

import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.domain.associate.Associate;
import com.sicredi.agenda.domain.session.Sessions;
import com.sicredi.agenda.domain.vote.Vote;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class AgendaTest {

    @Nested
    class GetId {

        @Test
        void returnsEmptyWhenIdIsNull() {
            final Agenda agenda = TestFixtures.AGENDA.toBuilder()
                    .id(null)
                    .build();

            assertThat(agenda.getId()).isEmpty();
        }

        @Test
        void returnsPresentWhenIdExists() {
            assertThat(TestFixtures.AGENDA.getId()).isPresent().contains(TestFixtures.AGENDA_ID);
        }
    }

    @Nested
    class AddSession {

        @Test
        void addsSessionToEmptySessionsList() {
            final Agenda emptyAgenda = TestFixtures.AGENDA.toBuilder()
                    .sessions(Sessions.ofEmpty())
                    .build();

            final Agenda result = emptyAgenda.addSession(TestFixtures.SESSION);

            assertThat(result.getSessions().getSessions()).hasSize(1);
            assertThat(result.getSessions().getSessions()).containsExactly(TestFixtures.SESSION);
        }
    }

    @Nested
    class VoteTest {

        @Test
        void addsMultipleVotesToSession() {
            final Clock clock = Clock.fixed(TestFixtures.TEN, ZoneOffset.UTC);
            final Vote firstVote = TestFixtures.VOTE;
            final Vote secondVote = Vote.builder()
                    .inFavor(false)
                    .associate(Associate.builder().cpf("98765432100").build())
                    .build();

            final Agenda result = TestFixtures.AGENDA.vote(TestFixtures.SESSION_ID, firstVote, clock)
                    .vote(TestFixtures.SESSION_ID, secondVote, clock);

            assertThat(result.getSessions().find(TestFixtures.SESSION_ID).getSessionVotes()).containsExactly(firstVote, secondVote);
        }
    }
}