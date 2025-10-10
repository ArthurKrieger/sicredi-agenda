package com.sicredi.agenda.domain.session;

import lombok.Builder;

import java.util.UUID;

@Builder
public record SessionId(String id) {

    public static SessionId newId() {
        return SessionId.builder().id(UUID.randomUUID().toString()).build();
    }

    public static SessionId of(String id) {
        return SessionId.builder().id(id).build();
    }
}