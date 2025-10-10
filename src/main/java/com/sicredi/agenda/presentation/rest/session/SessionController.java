package com.sicredi.agenda.presentation.rest.session;

import com.sicredi.agenda.application.SessionApplicationService;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.SessionId;
import com.sicredi.agenda.domain.vote.Vote;
import com.sicredi.agenda.presentation.rest.UrlConstants;
import com.sicredi.agenda.presentation.rest.vote.VoteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlConstants.SESSION_BASE_URL)
public class SessionController {

    private final ConversionService conversionService;

    private final SessionApplicationService sessionApplicationService;


    @PostMapping
    public ResponseEntity<SessionDto> createSession(@PathVariable("agenda-id") final String agendaId,
                                                    @RequestParam(value = "duration", required = false) final Optional<String> isoDuration) {
        final Duration duration = isoDuration.map(Duration::parse).orElse(null);
        final Session session = sessionApplicationService.createSession(AgendaId.of(agendaId), duration);
        final SessionDto sessionDto = conversionService.convert(session, SessionDto.class);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sessionDto);
    }

    @PostMapping("/{session-id}/votes")
    public ResponseEntity<Void> vote(@PathVariable("agenda-id") final String agendaId,
                                     @PathVariable("session-id") final String sessionId,
                                     @RequestBody final VoteDto voteDto) {
        final Vote vote = conversionService.convert(voteDto, Vote.class);
        sessionApplicationService.voteOnSession(AgendaId.of(agendaId), SessionId.of(sessionId), vote);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}
