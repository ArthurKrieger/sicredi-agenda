package com.sicredi.agenda.infrastructure.database.session;

import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.infrastructure.database.vote.VoteEntity;
import com.sicredi.agenda.infrastructure.database.vote.VoteToEntityConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionToEntityConverter implements Converter<Session, SessionEntity> {

    private final VoteToEntityConverter voteConverter;

    @Override
    public SessionEntity convert(Session session) {
        final List<VoteEntity> votes = session.getSessionVotes().stream()
                .map(voteConverter::convert)
                .toList();
        return SessionEntity.builder()
                .id(UUID.fromString(session.getId().id()))
                .startTime(session.getStart())
                .duration(session.getDuration())
                .votes(votes)
                .build();
    }
}