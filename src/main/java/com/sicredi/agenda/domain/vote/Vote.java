package com.sicredi.agenda.domain.vote;

import com.sicredi.agenda.domain.associate.Associate;
import lombok.Builder;

@Builder(toBuilder = true)
public record Vote(boolean inFavor, Associate associate) {
}
