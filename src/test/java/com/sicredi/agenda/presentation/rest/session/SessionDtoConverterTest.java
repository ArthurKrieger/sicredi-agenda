package com.sicredi.agenda.presentation.rest.session;

import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.SessionId;
import com.sicredi.agenda.domain.vote.Vote;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SessionDtoConverterTest {

    private static final Instant TWELVE = Instant.parse("2023-01-01T12:00:00Z");
    private static final Instant ELEVEN_FORTY_FIVE = Instant.parse("2023-01-01T11:45:00Z");
    private static final Instant TWELVE_FIFTEEN = Instant.parse("2023-01-01T12:15:00Z");
    private static final Duration THIRTY_MINUTES = Duration.ofMinutes(30);
    private static final Duration FIFTEEN_MINUTES = Duration.ofMinutes(15);
    private final Clock clock = Clock.fixed(TWELVE, ZoneId.systemDefault());
    private final SessionDtoConverter converter = new SessionDtoConverter(clock);

    @Nested
    class Convert {

        @Test
        public void createOpenSession() {
            final List<Vote> inFavor = Collections.nCopies(3, TestFixtures.VOTE.toBuilder().inFavor(true).build());
            final List<Vote> against = Collections.nCopies(5, TestFixtures.VOTE.toBuilder().inFavor(false).build());
            final Session session = TestFixtures.SESSION.toBuilder()
                    .id(SessionId.of("id"))
                    .sessionVotes(Stream.concat(inFavor.stream(), against.stream()).toList())
                    .start(ELEVEN_FORTY_FIVE)
                    .duration(THIRTY_MINUTES)
                    .build();

            final SessionDto sessionDto = converter.convert(session);

            assertThat(sessionDto.id()).isEqualTo(session.getId().id());
            assertThat(sessionDto.startTime()).isEqualTo(session.getStart());
            assertThat(sessionDto.endTime()).isEqualTo(TWELVE_FIFTEEN);
            assertThat(sessionDto.remainingTime()).isEqualTo(FIFTEEN_MINUTES);
            assertThat(sessionDto.favorCount()).isEqualTo(3);
            assertThat(sessionDto.againstCount()).isEqualTo(5);
        }

        @Test
        public void createClosedSession() {
            final List<Vote> inFavor = Collections.nCopies(3, TestFixtures.VOTE.toBuilder().inFavor(true).build());
            final List<Vote> against = Collections.nCopies(5, TestFixtures.VOTE.toBuilder().inFavor(false).build());
            final Session session = TestFixtures.SESSION.toBuilder()
                    .id(SessionId.of("id"))
                    .sessionVotes(Stream.concat(inFavor.stream(), against.stream()).toList())
                    .start(TWELVE)
                    .duration(FIFTEEN_MINUTES)
                    .build();

            final SessionDto sessionDto = converter.convert(session);

            assertThat(sessionDto.id()).isEqualTo(session.getId().id());
            assertThat(sessionDto.startTime()).isEqualTo(session.getStart());
            assertThat(sessionDto.endTime()).isEqualTo(TWELVE_FIFTEEN);
            assertThat(sessionDto.remainingTime()).isEqualTo(FIFTEEN_MINUTES);
            assertThat(sessionDto.favorCount()).isEqualTo(3);
            assertThat(sessionDto.againstCount()).isEqualTo(5);
        }
    }

}
