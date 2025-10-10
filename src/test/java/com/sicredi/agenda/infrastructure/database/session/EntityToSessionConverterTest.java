package com.sicredi.agenda.infrastructure.database.session;

import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.SessionId;
import com.sicredi.agenda.domain.vote.Vote;
import com.sicredi.agenda.infrastructure.database.vote.EntityToVoteConverter;
import com.sicredi.agenda.infrastructure.database.vote.VoteEntity;
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
class EntityToSessionConverterTest {

    @Mock
    private EntityToVoteConverter voteConverter;

    @InjectMocks
    private EntityToSessionConverter entityToSessionConverter;

    @Test
    void convertsSessionEntityToSession() {
        final UUID sessionId = UUID.randomUUID();
        final Instant startTime = Instant.parse("2023-01-01T10:00:00Z");
        final Duration duration = Duration.ofMinutes(30);
        final VoteEntity voteEntity = VoteEntity.builder().build();
        final Vote vote = Vote.builder().build();

        final SessionEntity sessionEntity = SessionEntity.builder()
                .id(sessionId)
                .startTime(startTime)
                .duration(duration)
                .votes(List.of(voteEntity))
                .build();

        when(voteConverter.convert(voteEntity)).thenReturn(vote);

        final Session result = entityToSessionConverter.convert(sessionEntity);

        assertThat(result.getId()).isEqualTo(SessionId.of(sessionId.toString()));
        assertThat(result.getStart()).isEqualTo(startTime);
        assertThat(result.getDuration()).isEqualTo(duration);
        assertThat(result.getSessionVotes()).hasSize(1);
        assertThat(result.getSessionVotes()).anySatisfy(convertedVote ->
                assertThat(convertedVote).isEqualTo(vote)
        );
    }

    @Test
    void convertsSessionEntityWithoutVotes() {
        final UUID sessionId = UUID.randomUUID();
        final SessionEntity sessionEntity = SessionEntity.builder()
                .id(sessionId)
                .startTime(Instant.parse("2023-01-01T14:00:00Z"))
                .duration(Duration.ofMinutes(15))
                .votes(List.of())
                .build();

        final Session result = entityToSessionConverter.convert(sessionEntity);

        assertThat(result.getId()).isEqualTo(SessionId.of(sessionId.toString()));
        assertThat(result.getSessionVotes()).isEmpty();
    }
}