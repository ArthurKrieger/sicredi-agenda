package com.sicredi.agenda.domain.exception;

public class AgendaException extends RuntimeException {

    public AgendaException(String message) {
        super(message);
    }

    public Object[] getMessageArgs() {
        return new Object[]{};
    }

}
