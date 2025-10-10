package com.sicredi.agenda.infrastructure.database.session;

import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.session.SessionId;
import com.sicredi.agenda.domain.vote.Vote;
import com.sicredi.agenda.infrastructure.database.vote.EntityToVoteConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EntityToSessionConverter implements Converter<SessionEntity, Session> {

    private final EntityToVoteConverter voteConverter;

    @Override
    public Session convert(SessionEntity entity) {
        final List<Vote> votes = entity.votes().stream()
                .map(voteConverter::convert)
                .toList();
        return Session.builder()
                .id(SessionId.of(entity.id().toString()))
                .start(entity.startTime())
                .duration(entity.duration())
                .sessionVotes(votes)
                .build();
    }
}