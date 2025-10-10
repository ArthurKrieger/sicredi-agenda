package com.sicredi.agenda.presentation.rest.agenda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.agenda.TestClockConfig;
import com.sicredi.agenda.TestConversionConfig;
import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.application.AgendaApplicationService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AgendaController.class)
@ContextConfiguration(classes = {TestClockConfig.class, TestConversionConfig.class})
class AgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AgendaApplicationService agendaApplicationService;


    @Nested
    class CreateAgenda {

        @Test
        void shouldCreateAgenda() throws Exception {
            final AgendaDto requestBody = AgendaDto.builder()
                    .description("Test agenda description")
                    .build();
            when(agendaApplicationService.saveAgenda(any())).thenReturn(TestFixtures.AGENDA);

            mockMvc.perform(post("/api/v1/agendas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestBody)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(TestFixtures.AGENDA_ID.id()));
        }
    }

    @Nested
    class GetAgendaById {

        @Test
        void shouldReturnAgendaWhenFound() throws Exception {
            when(agendaApplicationService.findById(eq(TestFixtures.AGENDA_ID))).thenReturn(TestFixtures.AGENDA);

            mockMvc.perform(get("/api/v1/agendas/{id}", TestFixtures.AGENDA_ID.id()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(TestFixtures.AGENDA_ID.id()))
                    .andExpect(jsonPath("$.description").value("Test agenda description"))
                    .andExpect(jsonPath("$.sessions").isNotEmpty());
        }
    }
}
