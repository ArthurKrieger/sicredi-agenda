package com.sicredi.agenda.infrastructure.database.vote;

import com.sicredi.agenda.domain.associate.Associate;
import com.sicredi.agenda.domain.vote.Vote;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToVoteConverter implements Converter<VoteEntity, Vote> {

    @Override
    public Vote convert(VoteEntity entity) {
        final Associate associate = Associate.builder()
                .cpf(entity.cpf())
                .build();
        return Vote.builder()
                .associate(associate)
                .inFavor(entity.inFavor())
                .build();
    }
}