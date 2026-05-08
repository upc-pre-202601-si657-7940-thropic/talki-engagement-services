package com.thropic.talki.gamification.infrastructure.messaging.producer;

import com.thropic.talki.gamification.domain.event.AchievementUnlockedEvent;
import com.thropic.talki.gamification.infrastructure.messaging.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class AchievementUnlockedPublisher {

    private static final Logger log = LoggerFactory.getLogger(AchievementUnlockedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public AchievementUnlockedPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(AchievementUnlockedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_OUT,
                event
        );
        log.info("[gamification-service] Published achievement.unlocked — userId={} achievement={}",
                event.getUserId(), event.getAchievementName());
    }
}
