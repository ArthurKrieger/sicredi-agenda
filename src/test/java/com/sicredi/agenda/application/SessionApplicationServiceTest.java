package com.sicredi.agenda.application;

import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.application.validation.SessionVoteValidator;
import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaRepository;
import com.sicredi.agenda.domain.exception.AgendaNotFoundException;
import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.SessionCreatedEvent;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Clock;
import java.time.Duration;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SessionApplicationServiceTest {

    private final SessionVoteValidator validator = mock(SessionVoteValidator.class);
    private final AgendaRepository agendaRepository = mock(AgendaRepository.class);
    private final ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private final Clock clock = Clock.fixed(TestFixtures.TEN, ZoneOffset.UTC);

    private final SessionApplicationService sessionApplicationService = new SessionApplicationService(
            List.of(validator),
            agendaRepository,
            applicationEventPublisher,
            clock
    );

    @Nested
    class CreateSession {

        @Test
        void createSession_agendaExists_createsAndReturnsSession() {
            final Duration duration = Duration.ofMinutes(5);
            final Agenda agenda = mock(Agenda.class);
            final Agenda agendaWithSession = mock(Agenda.class);

            when(agendaRepository.findAgenda(TestFixtures.AGENDA_ID)).thenReturn(Optional.of(agenda));
            when(agenda.addSession(any(Session.class))).thenReturn(agendaWithSession);
            when(agendaRepository.saveAgenda(agendaWithSession)).thenReturn(agendaWithSession);

            final Session result = sessionApplicationService.createSession(TestFixtures.AGENDA_ID, duration);

            assertThat(result).isNotNull();
            verify(agendaRepository).findAgenda(TestFixtures.AGENDA_ID);
            verify(agenda).addSession(any(Session.class));
            verify(agendaRepository).saveAgenda(agendaWithSession);
            verify(applicationEventPublisher).publishEvent(any(SessionCreatedEvent.class));
        }

        @Test
        void createSession_agendaNotFound_throwsAgendaNotFoundException() {
            final Duration duration = Duration.ofMinutes(5);

            when(agendaRepository.findAgenda(TestFixtures.AGENDA_ID)).thenReturn(Optional.empty());

            assertThatExceptionOfType(AgendaNotFoundException.class)
                    .isThrownBy(() -> sessionApplicationService.createSession(TestFixtures.AGENDA_ID, duration));

            verify(agendaRepository).findAgenda(TestFixtures.AGENDA_ID);
        }
    }

    @Nested
    class VoteOnSession {

        @Test
        void voteOnSession_validVote_callsValidatorsAndUpdatesAgenda() {
            final Agenda agenda = mock(Agenda.class);
            final Agenda updatedAgenda = mock(Agenda.class);

            when(agendaRepository.findAgenda(TestFixtures.AGENDA_ID)).thenReturn(Optional.of(agenda));
            when(agenda.vote(TestFixtures.SESSION_ID, TestFixtures.VOTE)).thenReturn(updatedAgenda);
            when(agendaRepository.saveAgenda(updatedAgenda)).thenReturn(updatedAgenda);

            sessionApplicationService.voteOnSession(TestFixtures.AGENDA_ID, TestFixtures.SESSION_ID, TestFixtures.VOTE);

            verify(validator).validate(agenda, TestFixtures.SESSION_ID, TestFixtures.VOTE);
            verify(agendaRepository).findAgenda(TestFixtures.AGENDA_ID);
            verify(agenda).vote(TestFixtures.SESSION_ID, TestFixtures.VOTE);
            verify(agendaRepository).saveAgenda(updatedAgenda);
        }

        @Test
        void voteOnSession_validatorFails_noRepositoryInteractions() {
            final Agenda agenda = mock(Agenda.class);

            when(agendaRepository.findAgenda(TestFixtures.AGENDA_ID)).thenReturn(Optional.of(agenda));
            doThrow(new RuntimeException("Validation failed")).when(validator).validate(agenda, TestFixtures.SESSION_ID, TestFixtures.VOTE);

            assertThatExceptionOfType(RuntimeException.class)
                    .isThrownBy(() -> sessionApplicationService.voteOnSession(TestFixtures.AGENDA_ID, TestFixtures.SESSION_ID, TestFixtures.VOTE))
                    .withMessage("Validation failed");

            verify(validator).validate(agenda, TestFixtures.SESSION_ID, TestFixtures.VOTE);
            verify(agendaRepository).findAgenda(TestFixtures.AGENDA_ID);
            verify(agenda, never()).vote(any(), any());
            verify(agendaRepository, never()).saveAgenda(any(Agenda.class));
        }

        @Test
        void voteOnSession_agendaNotFound_throwsAgendaNotFoundException() {
            when(agendaRepository.findAgenda(TestFixtures.AGENDA_ID)).thenReturn(Optional.empty());

            assertThatExceptionOfType(AgendaNotFoundException.class)
                    .isThrownBy(() -> sessionApplicationService.voteOnSession(TestFixtures.AGENDA_ID, TestFixtures.SESSION_ID, TestFixtures.VOTE));

            verify(agendaRepository).findAgenda(TestFixtures.AGENDA_ID);
        }
    }
}