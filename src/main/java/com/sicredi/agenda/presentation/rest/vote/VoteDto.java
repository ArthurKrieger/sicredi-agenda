package com.sicredi.agenda.presentation.rest.vote;

import lombok.Builder;

@Builder
public record VoteDto(VoteValue vote, String cpf) {
}
