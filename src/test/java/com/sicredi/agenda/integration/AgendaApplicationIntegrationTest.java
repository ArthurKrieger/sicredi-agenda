package com.sicredi.agenda.integration;

import com.mercateo.test.clock.TestClock;
import com.sicredi.agenda.TestClockConfig;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.presentation.rest.agenda.AgendaDto;
import com.sicredi.agenda.presentation.rest.session.SessionDto;
import com.sicredi.agenda.presentation.rest.vote.VoteDto;
import com.sicredi.agenda.presentation.rest.vote.VoteValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;

import java.time.Clock;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TestClockConfig.class})
class AgendaApplicationIntegrationTest {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Clock testClock;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }


    @Test
    void shouldCreateAgendaCreateSessionAndVoteSuccessfully() {
        final String agendaId = createAgenda("Integration Test Agenda");
        final SessionDto session = createSession(agendaId, "PT15M");

        final ResponseEntity<Void> voteResponse = vote(agendaId, session.id(), "12345678900", VoteValue.SIM);
        assertThat(voteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldHandleSessionVotingWithClockAdvances() {
        final String agendaId = createAgenda("Session Expiration Flow Test");
        final SessionDto session = createSession(agendaId, "PT1M");

        final ResponseEntity<Void> firstVote = vote(agendaId, session.id(), "12345678900", VoteValue.SIM);
        assertThat(firstVote.getStatusCode()).isEqualTo(HttpStatus.OK);

        ((TestClock) testClock).fastForward(Duration.ofSeconds(30));
        final ResponseEntity<Void> secondVote = vote(agendaId, session.id(), "98765432100", VoteValue.NAO);
        assertThat(secondVote.getStatusCode()).isEqualTo(HttpStatus.OK);

        ((TestClock) testClock).fastForward(Duration.ofSeconds(45));
        final ResponseEntity<Void> lateVote = vote(agendaId, session.id(), "12345678900", VoteValue.SIM);
        assertThat(lateVote.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        final AgendaDto agendaAfterVotes = getAgenda(agendaId);
        assertThat(agendaAfterVotes.sessions())
                .singleElement()
                .satisfies(sessionDto -> {
                    assertThat(sessionDto.endTime()).isBeforeOrEqualTo(sessionDto.startTime().plus(Duration.ofMinutes(1)));
                    assertThat(sessionDto.remainingTime()).isEqualTo(Duration.ZERO);
                    assertThat(sessionDto.favorCount()).isEqualTo(1);
                    assertThat(sessionDto.againstCount()).isEqualTo(1);
                });
    }

    private String createAgenda(String description) {
        final AgendaDto request = AgendaDto.builder().description(description).build();
        final ResponseEntity<AgendaId> response = restTemplate.postForEntity(
                baseUrl + "/api/v1/agendas",
                request,
                AgendaId.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return response.getBody().id();
    }

    private SessionDto createSession(String agendaId, String duration) {
        final ResponseEntity<SessionDto> response = restTemplate.postForEntity(
                baseUrl + "/api/v1/agendas/{agendaId}/sessions?duration=" + duration,
                null,
                SessionDto.class,
                agendaId
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

    private ResponseEntity<Void> vote(String agendaId, String sessionId, String cpf, VoteValue voteValue) {
        final VoteDto voteRequest = VoteDto.builder().cpf(cpf).vote(voteValue).build();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<VoteDto> entity = new HttpEntity<>(voteRequest, headers);

        return restTemplate.postForEntity(
                baseUrl + "/api/v1/agendas/{agendaId}/sessions/{sessionId}/votes",
                entity,
                Void.class,
                agendaId,
                sessionId
        );
    }

    private AgendaDto getAgenda(String agendaId) {
        final ResponseEntity<AgendaDto> response = restTemplate.getForEntity(
                baseUrl + "/api/v1/agendas/{agendaId}",
                AgendaDto.class,
                agendaId
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

}
