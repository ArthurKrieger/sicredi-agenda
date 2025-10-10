package com.sicredi.agenda.infrastructure.database.vote;


import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("vote")
@Builder
public record VoteEntity(
        @Id UUID id,
        UUID sessionId,
        String cpf,
        boolean inFavor
) {
}