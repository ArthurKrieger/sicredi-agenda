package com.sicredi.agenda.application.validation;

import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.exception.SessionNotOpenException;
import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.SessionId;
import com.sicredi.agenda.domain.vote.Vote;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
@RequiredArgsConstructor
public class SessionOpenValidator implements SessionVoteValidator {

    private final Clock clock;

    @Override
    public void validate(final @NonNull Agenda agenda, final @NonNull SessionId sessionId, final @NonNull Vote vote) {
        Session session = agenda.getSessions().find(sessionId);
        if (!session.isOpen(clock)) {
            throw new SessionNotOpenException();
        }
    }
}