package com.sicredi.agenda.presentation.rest.vote;

import com.sicredi.agenda.domain.associate.Associate;
import com.sicredi.agenda.domain.vote.Vote;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VoteConverter implements Converter<VoteDto, Vote> {

    @Override
    public Vote convert(VoteDto dto) {
        final Associate associate = Associate
                .builder()
                .cpf(dto.cpf())
                .build();
        return Vote.builder()
                .inFavor(VoteValue.SIM.equals(dto.vote()))
                .associate(associate)
                .build();
    }
}
