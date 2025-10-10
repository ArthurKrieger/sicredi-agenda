package com.sicredi.agenda.infrastructure.database.session;

import com.sicredi.agenda.infrastructure.database.vote.VoteEntity;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Table("session")
public record SessionEntity(
        @Id UUID id,
        UUID agendaId,
        Instant startTime,
        Duration duration,
        @MappedCollection(idColumn = "session_id") List<VoteEntity> votes
) {
}
