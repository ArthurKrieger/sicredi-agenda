package com.sicredi.agenda.presentation.rest;

import com.sicredi.agenda.domain.exception.AgendaException;
import com.sicredi.agenda.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResponseExceptionHandlerTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ResponseExceptionHandler exceptionHandler;

    @Nested
    class HandleGenericException {
        @Test
        void shouldReturnInternalServerError() {
            final var exception = new RuntimeException("Test error");

            final ResponseEntity<String> response = exceptionHandler.handleException(exception);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("Internal Server Error: Test error", response.getBody());
        }
    }

    @Nested
    class HandleAgendaException {
        @Test
        void shouldReturnBadRequestWithTranslatedMessage() {
            final var exception = new AgendaException("agenda.error");
            final var expectedMessage = "Translated error message";

            when(messageSource.getMessage(eq("agenda.error"), any(), eq("agenda.error"), any(Locale.class)))
                    .thenReturn(expectedMessage);

            final ResponseEntity<String> response = exceptionHandler.handleAgendaException(exception);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(expectedMessage, response.getBody());
            verify(messageSource).getMessage(eq("agenda.error"), any(), eq("agenda.error"), any(Locale.class));
        }
    }

    @Nested
    class HandleResourceNotFoundException {
        @Test
        void shouldReturnNotFoundWithTranslatedMessage() {
            final var exception = new ResourceNotFoundException("123");
            final var expectedMessage = "Resource 123 not found";

            when(messageSource.getMessage(eq("resource.not.found"), any(), eq("resource.not.found"), any(Locale.class)))
                    .thenReturn(expectedMessage);

            final ResponseEntity<String> response = exceptionHandler.validationException(exception);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals(expectedMessage, response.getBody());
            verify(messageSource).getMessage(eq("resource.not.found"), any(), eq("resource.not.found"), any(Locale.class));
        }
    }
}