package com.sicredi.agenda.domain.exception;

public class SessionNotOpenException extends AgendaException {
    private static final String SESSION_NOT_OPEN_EXCEPTION = "session.not.open";

    public SessionNotOpenException() {
        super(SESSION_NOT_OPEN_EXCEPTION);
    }
}