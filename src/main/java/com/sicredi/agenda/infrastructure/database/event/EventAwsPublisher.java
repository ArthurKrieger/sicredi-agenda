package com.sicredi.agenda.infrastructure.database.event;

import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EventAwsPublisher {

    private final EventJdbcRepository eventJdbcRepository;
    private final SnsTemplate snsTemplate;

    @Transactional
    @Scheduled(cron = "*/30 * * * * *")
    public void publishEvents() {
        eventJdbcRepository.findAll()
                .forEach(event -> {
                    snsTemplate.convertAndSend("agenda-events", event.payload());
                    eventJdbcRepository.markAsPublished(event.id());
                });
    }

}
