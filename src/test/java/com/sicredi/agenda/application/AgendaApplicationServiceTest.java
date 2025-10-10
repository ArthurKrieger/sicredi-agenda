package com.sicredi.agenda.application;

import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.domain.agenda.AgendaRepository;
import com.sicredi.agenda.domain.exception.AgendaNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendaApplicationServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @InjectMocks
    private AgendaApplicationService agendaApplicationService;


    @Nested
    class SaveAgenda {

        @Test
        void saveAgenda_repositoryIsCalled(){
            final Agenda savedAgenda = mock(Agenda.class);
            when(agendaRepository.saveAgenda(TestFixtures.AGENDA)).thenReturn(savedAgenda);

            final Agenda result = agendaApplicationService.saveAgenda(TestFixtures.AGENDA);

            assertThat(result).isEqualTo(savedAgenda);
            verify(agendaRepository).saveAgenda(TestFixtures.AGENDA);
        }
    }

    @Nested
    class FindById {

        @Test
        void findById_agendaExists_returnsAgenda() {
            final Agenda foundAgenda = mock(Agenda.class);
            when(agendaRepository.findAgenda(TestFixtures.AGENDA_ID)).thenReturn(Optional.of(foundAgenda));

            final Agenda result = agendaApplicationService.findById(TestFixtures.AGENDA_ID);

            assertThat(result).isEqualTo(foundAgenda);
            verify(agendaRepository).findAgenda(TestFixtures.AGENDA_ID);
        }

        @Test
        void findById_agendaNotFound_throwsAgendaNotFoundException() {
            when(agendaRepository.findAgenda(TestFixtures.AGENDA_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> agendaApplicationService.findById(TestFixtures.AGENDA_ID))
                    .isInstanceOf(AgendaNotFoundException.class);
            verify(agendaRepository).findAgenda(TestFixtures.AGENDA_ID);
        }
    }
}
