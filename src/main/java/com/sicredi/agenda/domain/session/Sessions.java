package com.sicredi.agenda.domain.session;


import com.sicredi.agenda.domain.exception.SessionNotFoundException;
import com.sicredi.agenda.domain.vote.Vote;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Builder(toBuilder = true)
public class Sessions {

    private static final Sessions EMPTY = Sessions.builder().build();

    @Builder.Default
    private final List<Session> sessions = new ArrayList<>();

    public static Sessions ofEmpty() {
        return EMPTY;
    }

    public Session find(SessionId sessionId) {
        return sessions.stream()
                .filter(session -> session.getId().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new SessionNotFoundException(sessionId));
    }

    private Sessions update(SessionId sessionId, Function<Session, Session> updateFunction) {
        Session existingSession = find(sessionId);
        Session updatedSession = updateFunction.apply(existingSession);

        List<Session> updatedSessions = sessions.stream()
                .map(session -> session.getId().equals(sessionId) ? updatedSession : session)
                .collect(Collectors.toList());

        return this.toBuilder()
                .sessions(updatedSessions)
                .build();
    }

    public Sessions addVote(SessionId sessionId, Vote vote, Clock clock) {
        return update(sessionId, session -> session.addVote(vote, clock));
    }

    public Sessions add(Session newSession) {
        List<Session> updatedSessions = new ArrayList<>(this.sessions);
        updatedSessions.add(newSession);
        return this.toBuilder()
                .sessions(updatedSessions)
                .build();
    }

}