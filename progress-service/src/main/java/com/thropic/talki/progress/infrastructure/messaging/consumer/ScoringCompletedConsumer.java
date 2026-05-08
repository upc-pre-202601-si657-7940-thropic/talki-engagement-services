package com.thropic.talki.progress.infrastructure.messaging.consumer;

import com.thropic.talki.progress.domain.event.ScoringCompletedEvent;
import com.thropic.talki.progress.domain.model.UserProgress;
import com.thropic.talki.progress.infrastructure.messaging.config.RabbitMQConfig;
import com.thropic.talki.progress.infrastructure.persistence.UserProgressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ScoringCompletedConsumer {

    private static final Logger log = LoggerFactory.getLogger(ScoringCompletedConsumer.class);

    private final UserProgressRepository progressRepository;

    public ScoringCompletedConsumer(UserProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void onScoringCompleted(ScoringCompletedEvent event) {
        log.info("[progress-service] Actualizando progreso — userId={} score={}",
                event.getUserId(), event.getOverallScore());

        UserProgress progress = progressRepository.findByUserId(event.getUserId())
                .orElseGet(() -> new UserProgress(event.getUserId()));

        progress.recordSession(event.getOverallScore(), event.getDurationSeconds());
        progressRepository.save(progress);

        log.info("[progress-service] Progreso guardado — userId={} totalSessions={} avg={}",
                event.getUserId(), progress.getTotalSessions(), progress.getAverageScore());
    }
}
