package com.thropic.talki.gamification.infrastructure.messaging.consumer;

import com.thropic.talki.gamification.domain.event.AchievementUnlockedEvent;
import com.thropic.talki.gamification.domain.event.ScoringCompletedEvent;
import com.thropic.talki.gamification.domain.model.UserStreak;
import com.thropic.talki.gamification.infrastructure.messaging.config.RabbitMQConfig;
import com.thropic.talki.gamification.infrastructure.messaging.producer.AchievementUnlockedPublisher;
import com.thropic.talki.gamification.infrastructure.persistence.UserStreakRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ScoringCompletedConsumer {

    private static final Logger log = LoggerFactory.getLogger(ScoringCompletedConsumer.class);

    private final UserStreakRepository streakRepository;
    private final AchievementUnlockedPublisher publisher;

    public ScoringCompletedConsumer(UserStreakRepository streakRepository,
                                     AchievementUnlockedPublisher publisher) {
        this.streakRepository = streakRepository;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_IN)
    public void onScoringCompleted(ScoringCompletedEvent event) {
        log.info("[gamification-service] Procesando score — userId={} score={}",
                event.getUserId(), event.getOverallScore());

        UserStreak streak = streakRepository.findByUserId(event.getUserId())
                .orElseGet(() -> new UserStreak(event.getUserId()));

        streak.recordSession(event.getOverallScore());
        streakRepository.save(streak);

        checkAndPublishAchievements(streak, event);
    }

    private void checkAndPublishAchievements(UserStreak streak, ScoringCompletedEvent event) {
        if (streak.getCurrentStreak() == 7) {
            publisher.publish(new AchievementUnlockedEvent(
                    event.getUserId(), "streak_7",
                    "Racha de 7 días", "Practicaste 7 días consecutivos"
            ));
        }
        if (streak.getTotalXp() >= 1000 && streak.getTotalXp() - event.getOverallScore() < 1000) {
            publisher.publish(new AchievementUnlockedEvent(
                    event.getUserId(), "xp_1000",
                    "1000 XP", "Alcanzaste 1000 puntos de experiencia"
            ));
        }
        if (event.getOverallScore() == 100) {
            publisher.publish(new AchievementUnlockedEvent(
                    event.getUserId(), "perfect_score",
                    "Puntuación perfecta", "Obtuviste 100/100 en una sesión"
            ));
        }
    }
}
