package com.sicredi.agenda.infrastructure.database.event;


import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface EventJdbcRepository extends CrudRepository<EventEntity, UUID> {
    @Query("SELECT * FROM event WHERE published = false ORDER BY occurred_on ASC")
    List<EventEntity> findUnpublishedEvents();

    @Modifying
    @Query("UPDATE event SET published = true WHERE id = :id")
    void markAsPublished(UUID id);
}
