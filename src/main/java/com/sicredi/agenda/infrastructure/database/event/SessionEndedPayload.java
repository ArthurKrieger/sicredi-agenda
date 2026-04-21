package com.sicredi.agenda.infrastructure.database.event;

import java.time.Instant;

public record SessionEndedPayload(
        String sessionId,
        Instant occurredAt,
        long favorVotes,
        long againstVotes
) {}
