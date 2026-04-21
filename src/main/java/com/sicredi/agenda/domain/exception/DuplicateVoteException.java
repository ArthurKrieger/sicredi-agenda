package com.sicredi.agenda.domain.exception;

public class DuplicateVoteException extends AgendaException {

    private static final String DUPLICATE_VOTE = "vote.duplicate";

    public DuplicateVoteException() {
        super(DUPLICATE_VOTE);
    }
}
