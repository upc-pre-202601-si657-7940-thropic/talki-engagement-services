package com.thropic.talki.gamification.infrastructure.messaging.consumer;

import com.thropic.talki.gamification.domain.event.AchievementUnlockedEvent;
import com.thropic.talki.gamification.domain.event.ScoringCompletedEvent;
import com.thropic.talki.gamification.domain.model.UserStreak;
import com.thropic.talki.gamification.infrastructure.messaging.producer.AchievementUnlockedPublisher;
import com.thropic.talki.gamification.infrastructure.persistence.UserStreakRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScoringCompletedConsumerTest {

    @Mock
    private UserStreakRepository streakRepository;

    @Mock
    private AchievementUnlockedPublisher publisher;

    @InjectMocks
    private ScoringCompletedConsumer consumer;

    private ScoringCompletedEvent baseEvent(String userId, int score) {
        ScoringCompletedEvent event = new ScoringCompletedEvent();
        event.setEventId("evt-" + userId);
        event.setEventType("scoring.completed");
        event.setOccurredAt(Instant.now());
        event.setSessionId("sess-" + userId);
        event.setUserId(userId);
        event.setOverallScore(score);
        return event;
    }

    @Test
    void onScoringCompleted_whenNoExistingStreak_shouldCreateOneAndSave() {
        when(streakRepository.findByUserId("user-1")).thenReturn(Optional.empty());

        consumer.onScoringCompleted(baseEvent("user-1", 80));

        ArgumentCaptor<UserStreak> captor = ArgumentCaptor.forClass(UserStreak.class);
        verify(streakRepository).save(captor.capture());
        UserStreak saved = captor.getValue();
        assertThat(saved.getUserId()).isEqualTo("user-1");
        assertThat(saved.getCurrentStreak()).isEqualTo(1);
        assertThat(saved.getTotalXp()).isEqualTo(80);
    }

    @Test
    void onScoringCompleted_whenExistingStreak_shouldAccumulateXp() {
        UserStreak existing = new UserStreak("user-1");
        ReflectionTestUtils.setField(existing, "totalXp", 200);
        ReflectionTestUtils.setField(existing, "currentStreak", 3);
        when(streakRepository.findByUserId("user-1")).thenReturn(Optional.of(existing));

        consumer.onScoringCompleted(baseEvent("user-1", 50));

        verify(streakRepository).save(existing);
        assertThat(existing.getTotalXp()).isEqualTo(250);
    }

    @Test
    void onScoringCompleted_whenStreakReaches7_shouldPublishStreak7Achievement() {
        UserStreak streak = new UserStreak("user-1");
        ReflectionTestUtils.setField(streak, "currentStreak", 6);
        ReflectionTestUtils.setField(streak, "lastSessionDate", LocalDate.now().minusDays(1));
        when(streakRepository.findByUserId("user-1")).thenReturn(Optional.of(streak));

        consumer.onScoringCompleted(baseEvent("user-1", 70));

        ArgumentCaptor<AchievementUnlockedEvent> captor =
                ArgumentCaptor.forClass(AchievementUnlockedEvent.class);
        verify(publisher).publish(captor.capture());
        assertThat(captor.getValue().getAchievementId()).isEqualTo("streak_7");
    }

    @Test
    void onScoringCompleted_whenPerfectScore_shouldPublishPerfectAchievement() {
        when(streakRepository.findByUserId("user-1")).thenReturn(Optional.empty());

        consumer.onScoringCompleted(baseEvent("user-1", 100));

        ArgumentCaptor<AchievementUnlockedEvent> captor =
                ArgumentCaptor.forClass(AchievementUnlockedEvent.class);
        verify(publisher).publish(captor.capture());
        assertThat(captor.getValue().getAchievementId()).isEqualTo("perfect_score");
    }

    @Test
    void onScoringCompleted_whenCrossing1000Xp_shouldPublishXp1000Achievement() {
        UserStreak streak = new UserStreak("user-1");
        ReflectionTestUtils.setField(streak, "totalXp", 950);
        when(streakRepository.findByUserId("user-1")).thenReturn(Optional.of(streak));

        consumer.onScoringCompleted(baseEvent("user-1", 80));

        ArgumentCaptor<AchievementUnlockedEvent> captor =
                ArgumentCaptor.forClass(AchievementUnlockedEvent.class);
        verify(publisher).publish(captor.capture());
        assertThat(captor.getValue().getAchievementId()).isEqualTo("xp_1000");
    }

    @Test
    void onScoringCompleted_whenAlreadyOver1000Xp_shouldNotPublishXp1000Again() {
        UserStreak streak = new UserStreak("user-1");
        ReflectionTestUtils.setField(streak, "totalXp", 1500);
        when(streakRepository.findByUserId("user-1")).thenReturn(Optional.of(streak));

        consumer.onScoringCompleted(baseEvent("user-1", 80));

        verify(publisher, never()).publish(any());
    }

    @Test
    void onScoringCompleted_whenScoreNeitherPerfectNorStreakNor1000Xp_shouldNotPublishAnyAchievement() {
        when(streakRepository.findByUserId("user-1")).thenReturn(Optional.empty());

        consumer.onScoringCompleted(baseEvent("user-1", 65));

        verify(streakRepository).save(any(UserStreak.class));
        verify(publisher, never()).publish(any());
    }
}
