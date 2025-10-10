package com.sicredi.agenda.domain.agenda;

import lombok.Builder;

import java.util.Objects;

@Builder
public record AgendaId(String id) {

    public AgendaId {
        Objects.requireNonNull(id, "Agenda id cannot be null");
    }

    public static AgendaId of(String id) {
        return AgendaId.builder().id(id).build();
    }
}
