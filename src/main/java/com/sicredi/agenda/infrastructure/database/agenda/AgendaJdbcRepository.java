package com.sicredi.agenda.infrastructure.database.agenda;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgendaJdbcRepository extends CrudRepository<AgendaEntity, UUID> {
    Optional<AgendaEntity> findById(UUID id);
}