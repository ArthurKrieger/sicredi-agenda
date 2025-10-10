package com.sicredi.agenda.presentation.rest;

import com.sicredi.agenda.domain.exception.AgendaException;
import com.sicredi.agenda.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ResponseExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error: " + ex.getMessage());
    }

    @ExceptionHandler(AgendaException.class)
    public ResponseEntity<String> handleAgendaException(AgendaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(messageSource.getMessage(ex.getMessage(), ex.getMessageArgs(), ex.getMessage(), LocaleContextHolder.getLocale()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> validationException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(messageSource.getMessage(ex.getMessage(), ex.getMessageArgs(), ex.getMessage(), LocaleContextHolder.getLocale()));
    }
}
