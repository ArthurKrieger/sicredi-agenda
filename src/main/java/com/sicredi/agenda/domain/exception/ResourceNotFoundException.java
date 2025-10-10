package com.sicredi.agenda.domain.exception;


import java.lang.reflect.Type;

public class ResourceNotFoundException extends AgendaException {
    private static final String RESOURCE_NOT_FOUND = "resource.not.found";

    private final String id;

    public ResourceNotFoundException( String id) {
        super(RESOURCE_NOT_FOUND);
        this.id = id;
    }

    public Object[] getMessageArgs() {
        return new Object[]{id};
    }
}