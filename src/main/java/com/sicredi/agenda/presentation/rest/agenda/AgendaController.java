package com.sicredi.agenda.presentation.rest.agenda;

import com.sicredi.agenda.application.AgendaApplicationService;
import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.agenda.AgendaId;
import com.sicredi.agenda.presentation.rest.UrlConstants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlConstants.BASE_URL)
public class AgendaController {

    private final ConversionService conversionService;

    private final AgendaApplicationService agendaApplicationService;

    @PostMapping
    public ResponseEntity<AgendaId> createAgenda(@RequestBody @NonNull final AgendaDto agendaDto) {
        final Agenda agenda = conversionService.convert(agendaDto, Agenda.class);
        final Agenda savedAgenda = agendaApplicationService.saveAgenda(agenda);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedAgenda.getId().orElse(null));
    }

    @GetMapping("/{agenda-id}")
    public ResponseEntity<AgendaDto> getAgendaById(@PathVariable("agenda-id") final String agendaId) {
        final Agenda agenda = agendaApplicationService.findById(AgendaId.of(agendaId));
        final AgendaDto agendaDto = conversionService.convert(agenda, AgendaDto.class);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(agendaDto);
    }
}
