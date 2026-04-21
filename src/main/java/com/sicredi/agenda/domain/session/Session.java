package com.sicredi.agenda.domain.session;

import com.sicredi.agenda.domain.exception.DuplicateVoteException;
import com.sicredi.agenda.domain.exception.SessionNotOpenException;
import com.sicredi.agenda.domain.vote.Vote;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Builder(toBuilder = true)
public class Session {

    private final SessionId id;
    private final Duration duration;
    @NonNull
    private final Instant start;
    private final List<Vote> sessionVotes;

    private Session(SessionId id, Duration duration, Instant start, List<Vote> sessionVotes) {
        this.id = Objects.requireNonNullElse(id, SessionId.newId());
        this.duration = Objects.requireNonNullElse(duration, Duration.ofMinutes(1));
        this.start = Objects.requireNonNull(start);
        this.sessionVotes = Objects.requireNonNullElse(sessionVotes, new ArrayList<>());
    }


    public Session addVote(Vote vote, Clock clock) {
        if (!isOpen(clock)) throw new SessionNotOpenException();
        boolean alreadyVoted = sessionVotes.stream()
                .anyMatch(v -> v.associate().equals(vote.associate()));
        if (alreadyVoted) throw new DuplicateVoteException();
        final List<Vote> updatedVotes = new ArrayList<>(this.sessionVotes);
        updatedVotes.add(vote);
        return this.toBuilder()
                .sessionVotes(updatedVotes)
                .build();
    }

    public Instant getEndTime() {
        return start.plus(duration);
    }

    public boolean isOpen(Clock clock) {
        final Instant now = Instant.now(clock);
        return !getEndTime().isBefore(now);
    }

}
