package com.sicredi.agenda.presentation.rest.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.agenda.TestClockConfig;
import com.sicredi.agenda.TestConversionConfig;
import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.application.SessionApplicationService;
import com.sicredi.agenda.domain.vote.Vote;
import com.sicredi.agenda.presentation.rest.vote.VoteDto;
import com.sicredi.agenda.presentation.rest.vote.VoteValue;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SessionController.class)
@ContextConfiguration(classes = {TestClockConfig.class, TestConversionConfig.class})
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SessionApplicationService sessionApplicationService;

    @Nested
    class CreateSession {

        @Test
        void shouldCreateSessionWithoutDuration() throws Exception {
            when(sessionApplicationService.createSession(eq(TestFixtures.AGENDA_ID), eq(null)))
                    .thenReturn(TestFixtures.SESSION);

            mockMvc.perform(post("/api/v1/agendas/{agenda-id}/sessions", TestFixtures.AGENDA_ID.id())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(TestFixtures.SESSION_ID.id()));
        }

        @Test
        void shouldCreateSessionWithDuration() throws Exception {
            when(sessionApplicationService.createSession(eq(TestFixtures.AGENDA_ID), eq(Duration.parse("PT10M"))))
                    .thenReturn(TestFixtures.SESSION);

            mockMvc.perform(post("/api/v1/agendas/{agenda-id}/sessions", TestFixtures.AGENDA_ID.id())
                            .param("duration", "PT10M")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(TestFixtures.SESSION_ID.id()));
        }
    }

    @Nested
    class VoteOnSession {

        @Test
        void shouldAcceptVote() throws Exception {
            final VoteDto voteDto = VoteDto.builder()
                    .cpf("12345678900")
                    .vote(VoteValue.SIM)
                    .build();

            doNothing().when(sessionApplicationService)
                    .voteOnSession(eq(TestFixtures.AGENDA_ID), eq(TestFixtures.SESSION_ID), any(Vote.class));

            mockMvc.perform(post("/api/v1/agendas/{agenda-id}/sessions/{session-id}/votes",
                            TestFixtures.AGENDA_ID.id(), TestFixtures.SESSION_ID.id())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(voteDto)))
                    .andExpect(status().isOk());
        }
    }
}
