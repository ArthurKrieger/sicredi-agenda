package com.sicredi.agenda.infrastructure.database.session;

import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.SessionId;
import com.sicredi.agenda.domain.vote.Vote;
import com.sicredi.agenda.infrastructure.database.vote.VoteEntity;
import com.sicredi.agenda.infrastructure.database.vote.VoteToEntityConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionToEntityConverterTest {

    @Mock
    private VoteToEntityConverter voteConverter;

    @InjectMocks
    private SessionToEntityConverter sessionToEntityConverter;

    @Test
    void convertsSessionToSessionEntity() {
        final String sessionId = UUID.randomUUID().toString();
        final Instant startTime = Instant.parse("2023-01-01T10:00:00Z");
        final Duration duration = Duration.ofMinutes(30);
        final Vote vote = Vote.builder().build();
        final VoteEntity voteEntity = VoteEntity.builder().build();

        final Session session = Session.builder()
                .id(SessionId.of(sessionId))
                .start(startTime)
                .duration(duration)
                .sessionVotes(List.of(vote))
                .build();

        when(voteConverter.convert(vote)).thenReturn(voteEntity);

        final SessionEntity result = sessionToEntityConverter.convert(session);

        assertThat(result.id()).isEqualTo(UUID.fromString(sessionId));
        assertThat(result.startTime()).isEqualTo(startTime);
        assertThat(result.duration()).isEqualTo(duration);
        assertThat(result.votes()).hasSize(1);
        assertThat(result.votes()).anySatisfy(convertedVote ->
                assertThat(convertedVote).isEqualTo(voteEntity)
        );
    }

    @Test
    void convertsSessionWithoutVotes() {
        final String sessionId = UUID.randomUUID().toString();
        final Session session = Session.builder()
                .id(SessionId.of(sessionId))
                .start(Instant.parse("2023-01-01T14:00:00Z"))
                .duration(Duration.ofMinutes(15))
                .sessionVotes(List.of())
                .build();

        final SessionEntity result = sessionToEntityConverter.convert(session);

        assertThat(result.id()).isEqualTo(UUID.fromString(sessionId));
        assertThat(result.votes()).isEmpty();
    }
}