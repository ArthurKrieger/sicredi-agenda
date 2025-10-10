package com.sicredi.agenda.presentation.rest.session;

import com.sicredi.agenda.domain.session.Session;
import com.sicredi.agenda.domain.vote.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class SessionDtoConverter implements Converter<Session, SessionDto> {

    private final Clock clock;

    @Override
    public SessionDto convert(Session session) {
        final Duration nowAndEndTimeDifference = Duration.between(Instant.now(clock), session.getEndTime());
        return SessionDto.builder()
                .id(session.getId().id())
                .startTime(session.getStart())
                .endTime(session.getEndTime())
                .remainingTime(nowAndEndTimeDifference.isNegative() ? Duration.ZERO : nowAndEndTimeDifference)
                .favorCount(countVotes(session, Vote::inFavor))
                .againstCount(countVotes(session, Predicate.not(Vote::inFavor)))
                .build();
    }

    private long countVotes(Session session, Predicate<Vote> filter) {
        return session.getSessionVotes().stream()
                .filter(filter)
                .count();
    }
}