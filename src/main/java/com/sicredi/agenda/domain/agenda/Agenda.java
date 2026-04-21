package com.sicredi.agenda.domain.agenda;

import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.SessionId;
import com.sicredi.agenda.domain.session.Sessions;
import com.sicredi.agenda.domain.vote.Vote;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.Clock;
import java.util.Optional;

@Getter
@Builder(toBuilder = true)
public class Agenda {

    private final AgendaId id;

    @NotEmpty
    private final String description;

    @Builder.Default
    private final Sessions sessions = Sessions.ofEmpty();

    public Optional<AgendaId> getId() {
        return Optional.ofNullable(id);
    }

    public Agenda addSession(@NonNull final Session session) {
        final Sessions updatedSessions = this.sessions.add(session);
        return this.toBuilder()
                .sessions(updatedSessions)
                .build();
    }

    public Agenda vote(@NonNull final SessionId sessionId, @NonNull final Vote vote, @NonNull final Clock clock) {
        final Sessions updatedSessions = this.sessions.addVote(sessionId, vote, clock);
        return this.toBuilder()
                .sessions(updatedSessions)
                .build();
    }

}
