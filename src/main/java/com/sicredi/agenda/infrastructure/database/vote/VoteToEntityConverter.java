package com.sicredi.agenda.infrastructure.database.vote;

import com.sicredi.agenda.domain.vote.Vote;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VoteToEntityConverter implements Converter<Vote, VoteEntity> {

    @Override
    public VoteEntity convert(Vote vote) {
        return VoteEntity.builder()
                .cpf(vote.associate().cpf())
                .inFavor(vote.inFavor())
                .build();

    }
}