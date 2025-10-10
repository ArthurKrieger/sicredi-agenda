package com.sicredi.agenda.application.validation;

import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.associate.AssociateValidator;
import com.sicredi.agenda.domain.session.SessionId;
import com.sicredi.agenda.domain.vote.Vote;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VoteValidator implements SessionVoteValidator {

    private final List<AssociateValidator> associateValidators;

    @Override
    public void validate(final @NonNull Agenda agenda, final @NonNull SessionId sessionId, final @NonNull Vote vote) {
        associateValidators.forEach(validator -> validator.validate(vote.associate()));
    }
}
