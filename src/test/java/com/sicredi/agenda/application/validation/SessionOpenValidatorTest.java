package com.sicredi.agenda.application.validation;

import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.exception.SessionNotOpenException;
import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.Sessions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionOpenValidatorTest {

    private final Session session = mock(Session.class);
    private final Agenda agenda = TestFixtures.AGENDA.toBuilder()
            .sessions(Sessions.builder().sessions(List.of(session)).build())
            .build();
    @Mock
    private Clock clock;
    @InjectMocks
    private SessionOpenValidator validator;

    @BeforeEach
    void setUp() {
        when(session.getId()).thenReturn(TestFixtures.SESSION_ID);
    }

    @Test
    void validationPassesWhenSessionIsOpen() {
        when(session.isOpen(clock)).thenReturn(true);

        validator.validate(agenda, TestFixtures.SESSION_ID, TestFixtures.VOTE);

        verify(session).isOpen(clock);
    }

    @Test
    void validationThrowsExceptionWhenSessionIsNotOpen() {
        when(session.isOpen(clock)).thenReturn(false);

        assertThatExceptionOfType(SessionNotOpenException.class)
                .isThrownBy(() -> validator.validate(agenda, TestFixtures.SESSION_ID, TestFixtures.VOTE));
    }
}