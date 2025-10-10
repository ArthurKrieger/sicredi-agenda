package com.sicredi.agenda;

import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.domain.associate.Associate;
import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.SessionId;
import com.sicredi.agenda.domain.session.Sessions;
import com.sicredi.agenda.domain.vote.Vote;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class TestFixtures {

    public static final Instant TEN = Instant.parse("2023-01-01T10:00:00Z");

    public static final AgendaId AGENDA_ID = AgendaId.builder()
            .id(UUID.randomUUID().toString())
            .build();

    public static final Associate ASSOCIATE = Associate.builder()
            .cpf("12345678901")
            .build();

    public static final Vote VOTE = Vote.builder()
            .inFavor(true)
            .associate(ASSOCIATE)
            .build();

    public static final SessionId SESSION_ID = SessionId.builder()
            .id(UUID.randomUUID().toString())
            .build();

    public static final Session SESSION = Session.builder()
            .id(SESSION_ID)
            .duration(Duration.ofMinutes(5))
            .start(TEN)
            .sessionVotes(List.of())
            .build();

    public static final Sessions SESSIONS = Sessions.builder()
            .sessions(List.of(SESSION))
            .build();

    public static final Agenda AGENDA = Agenda.builder()
            .id(AGENDA_ID)
            .description("Test agenda description")
            .sessions(SESSIONS)
            .build();
}