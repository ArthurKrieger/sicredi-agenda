package com.sicredi.agenda.infrastructure.database.agenda;

import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.domain.agenda.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JdbcBasedAgendaRepository implements AgendaRepository {

    private final AgendaJdbcRepository crudRepository;

    private final ConversionService conversionService;


    @Override
    public Agenda saveAgenda(Agenda agenda) {
        final AgendaEntity entity = conversionService.convert(agenda, AgendaEntity.class);
        final AgendaEntity saved = crudRepository.save(entity); // ID populated by DB
        return conversionService.convert(saved, Agenda.class); // map back to aggregate with ID
    }

    @Override
    public Optional<Agenda> findAgenda(AgendaId agendaId) {
        return crudRepository.findById(UUID.fromString(agendaId.id()))
                .map(entity -> conversionService.convert(entity, Agenda.class));
    }
}
