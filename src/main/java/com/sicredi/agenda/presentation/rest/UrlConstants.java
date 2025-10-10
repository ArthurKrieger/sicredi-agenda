package com.sicredi.agenda.presentation.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrlConstants {

    public static final String BASE_URL = "/api/v1/agendas";

    public static final String SESSION_BASE_URL = BASE_URL + "/{agenda-id}/sessions";
}
