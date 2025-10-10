package com.sicredi.agenda.application;

import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.domain.agenda.AgendaRepository;
import com.sicredi.agenda.domain.exception.AgendaNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgendaApplicationService {

    private final AgendaRepository agendaRepository;

    public Agenda saveAgenda(final @NonNull Agenda agenda) {
        return agendaRepository.saveAgenda(agenda);
    }

    public Agenda findById(final @NonNull AgendaId agendaId) {
        return agendaRepository.findAgenda(agendaId).orElseThrow(() -> new AgendaNotFoundException(agendaId));
    }

}
