package com.sicredi.agenda.domain.exception;

import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.SessionId;

public class SessionNotFoundException extends ResourceNotFoundException {
    public SessionNotFoundException(SessionId sessionId) {
        super( sessionId.id());
    }
}
