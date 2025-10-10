package com.sicredi.agenda.application;

import com.sicredi.agenda.application.validation.SessionVoteValidator;
import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.domain.agenda.AgendaRepository;
import com.sicredi.agenda.domain.exception.AgendaNotFoundException;
import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.SessionCreatedEvent;
import com.sicredi.agenda.domain.session.SessionId;
import com.sicredi.agenda.domain.vote.Vote;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionApplicationService {

    private final List<SessionVoteValidator> sessionVoteValidators;
    private final AgendaRepository agendaRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Clock clock;

    @Transactional
    public Session createSession(@NonNull final AgendaId agendaId, @Nullable final Duration duration) {
        final Agenda agenda = agendaRepository.findAgenda(agendaId).orElseThrow(() -> new AgendaNotFoundException(agendaId));
        final Session session = Session.builder()
                .start(Instant.now(clock))
                .duration(duration)
                .build();
        final Agenda agendaWithSession = agenda.addSession(session);
        agendaRepository.saveAgenda(agendaWithSession);
        applicationEventPublisher.publishEvent(new SessionCreatedEvent(session));
        return session;
    }
    
    @Transactional
    public void voteOnSession(@NonNull final AgendaId agendaId, @NonNull final SessionId sessionId, @NonNull final Vote vote) {
        final Agenda agenda = agendaRepository.findAgenda(agendaId).orElseThrow(() -> new AgendaNotFoundException(agendaId));
        sessionVoteValidators.forEach(validator -> validator.validate(agenda, sessionId, vote));
        final Agenda updatedAgenda = agenda.vote(sessionId, vote);
        agendaRepository.saveAgenda(updatedAgenda);
    }
}
