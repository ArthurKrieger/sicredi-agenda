package com.sicredi.agenda.infrastructure.database.agenda;

import com.sicredi.agenda.infrastructure.database.session.SessionEntity;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

@Builder
@Table("agenda")
public record AgendaEntity(
        @Id UUID id,
        String description,
        @MappedCollection(idColumn = "agenda_id") List<SessionEntity> sessions
) {}
