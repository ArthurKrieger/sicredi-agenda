package com.sicredi.agenda.domain.session;

import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.domain.associate.Associate;
import com.sicredi.agenda.domain.vote.Vote;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SessionTest {

    private static final Instant TEN = Instant.parse("2023-01-01T10:00:00Z");
    private static final Instant TEN_AND_TWO = Instant.parse("2023-01-01T10:02:00Z");
    private static final Instant TEN_AND_FIVE = Instant.parse("2023-01-01T10:05:00Z");
    private static final Instant TEN_AND_SIX = Instant.parse("2023-01-01T10:06:00Z");

    @Nested
    class AddVote {

        @Test
        void addsMultipleVotesToSession() {
            final Clock clock = Clock.fixed(TEN_AND_TWO, ZoneOffset.UTC);
            final Vote firstVote = TestFixtures.VOTE;
            final Vote secondVote = Vote.builder()
                    .inFavor(false)
                    .associate(Associate.builder().cpf("98765432100").build())
                    .build();

            final Session result = TestFixtures.SESSION.addVote(firstVote, clock)
                    .addVote(secondVote, clock);

            assertThat(result.getSessionVotes()).hasSize(2);
            assertThat(result.getSessionVotes()).containsExactly(firstVote, secondVote);
        }
    }

    @Nested
    class GetEndTime {

        @Test
        void returnsStartPlusDuration() {
            final Instant start = mock(Instant.class);
            final Duration duration = mock(Duration.class);
            final Instant expectedEndTime = mock(Instant.class);
            final Session session = TestFixtures.SESSION.toBuilder()
                    .start(start)
                    .duration(duration)
                    .build();

            when(start.plus(duration)).thenReturn(expectedEndTime);

            final Instant endTime = session.getEndTime();

            assertThat(endTime).isEqualTo(expectedEndTime);
            verify(start).plus(duration);
        }
    }

    @Nested
    class IsOpen {

        @Test
        void returnsTrueWhenCurrentTimeIsBeforeEndTime() {
            final Session session = TestFixtures.SESSION.toBuilder()
                    .start(TEN)
                    .duration(Duration.ofMinutes(5))
                    .build();
            final Clock clock = Clock.fixed(TEN_AND_TWO, ZoneOffset.UTC);

            final boolean isOpen = session.isOpen(clock);

            assertThat(isOpen).isTrue();
        }

        @Test
        void returnsFalseWhenCurrentTimeIsAfterEndTime() {
            final Session session = TestFixtures.SESSION.toBuilder()
                    .start(TEN)
                    .duration(Duration.ofMinutes(5))
                    .build();
            final Clock clock = Clock.fixed(TEN_AND_SIX, ZoneOffset.UTC);

            final boolean isOpen = session.isOpen(clock);

            assertThat(isOpen).isFalse();
        }

        @Test
        void returnsTruesWhenCurrentTimeEqualsEndTime() {
            final Session session = TestFixtures.SESSION.toBuilder()
                    .start(TEN)
                    .duration(Duration.ofMinutes(5))
                    .build();
            final Clock clock = Clock.fixed(TEN_AND_FIVE, ZoneOffset.UTC);

            final boolean isOpen = session.isOpen(clock);

            assertThat(isOpen).isTrue();
        }
    }
}