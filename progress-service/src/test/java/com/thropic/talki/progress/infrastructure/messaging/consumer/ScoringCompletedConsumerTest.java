package com.thropic.talki.progress.infrastructure.messaging.consumer;

import com.thropic.talki.progress.domain.event.ScoringCompletedEvent;
import com.thropic.talki.progress.domain.model.UserProgress;
import com.thropic.talki.progress.infrastructure.persistence.UserProgressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScoringCompletedConsumerTest {

    @Mock
    private UserProgressRepository progressRepository;

    @InjectMocks
    private ScoringCompletedConsumer consumer;

    @Test
    void onScoringCompleted_whenUserHasNoProgress_shouldCreateNew() {
        when(progressRepository.findByUserId("user-001")).thenReturn(Optional.empty());
        when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        consumer.onScoringCompleted(buildEvent("user-001", 85, 300));

        ArgumentCaptor<UserProgress> captor = ArgumentCaptor.forClass(UserProgress.class);
        verify(progressRepository).save(captor.capture());
        assertThat(captor.getValue().getTotalSessions()).isEqualTo(1);
        assertThat(captor.getValue().getBestScore()).isEqualTo(85);
    }

    @Test
    void onScoringCompleted_whenUserAlreadyHasProgress_shouldUpdateExisting() {
        UserProgress existing = new UserProgress("user-002");
        existing.recordSession(70, 300);
        when(progressRepository.findByUserId("user-002")).thenReturn(Optional.of(existing));
        when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        consumer.onScoringCompleted(buildEvent("user-002", 90, 600));

        ArgumentCaptor<UserProgress> captor = ArgumentCaptor.forClass(UserProgress.class);
        verify(progressRepository).save(captor.capture());
        assertThat(captor.getValue().getTotalSessions()).isEqualTo(2);
        assertThat(captor.getValue().getBestScore()).isEqualTo(90);
    }

    @Test
    void onScoringCompleted_shouldAlwaysPersistProgress() {
        when(progressRepository.findByUserId(any())).thenReturn(Optional.empty());
        when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        consumer.onScoringCompleted(buildEvent("user-003", 60, 180));

        verify(progressRepository, times(1)).save(any(UserProgress.class));
    }

    private ScoringCompletedEvent buildEvent(String userId, int score, int duration) {
        ScoringCompletedEvent event = new ScoringCompletedEvent();
        event.setEventId("evt-" + userId);
        event.setEventType("scoring.completed");
        event.setOccurredAt(Instant.now());
        event.setSessionId("session-" + userId);
        event.setUserId(userId);
        event.setOverallScore(score);
        event.setDurationSeconds(duration);
        event.setSilenceRatio(0.07);
        event.setWordsPerMinute(140.0);
        return event;
    }
}
