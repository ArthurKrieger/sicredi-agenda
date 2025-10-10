package com.sicredi.agenda.application.validation;

import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.session.SessionId;
import com.sicredi.agenda.domain.vote.Vote;

public interface SessionVoteValidator {
    void validate(Agenda agenda, SessionId sessionId, Vote vote);
}