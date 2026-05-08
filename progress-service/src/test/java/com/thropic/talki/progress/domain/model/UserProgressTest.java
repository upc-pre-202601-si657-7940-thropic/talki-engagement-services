package com.thropic.talki.progress.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserProgressTest {

    private UserProgress progress;

    @BeforeEach
    void setUp() {
        progress = new UserProgress("user-manuel-001");
    }

    @Test
    void recordSession_whenFirstSession_shouldInitializeCounters() {
        progress.recordSession(80, 300);

        assertThat(progress.getTotalSessions()).isEqualTo(1);
        assertThat(progress.getTotalMinutes()).isEqualTo(5);
        assertThat(progress.getAverageScore()).isEqualTo(80.0);
        assertThat(progress.getBestScore()).isEqualTo(80);
        assertThat(progress.getCurrentStreak()).isEqualTo(1);
    }

    @Test
    void recordSession_whenMultipleSessions_shouldAccumulateCorrectly() {
        progress.recordSession(60, 180);
        progress.recordSession(80, 300);
        progress.recordSession(100, 600);

        assertThat(progress.getTotalSessions()).isEqualTo(3);
        assertThat(progress.getTotalMinutes()).isEqualTo(18);
        assertThat(progress.getAverageScore()).isCloseTo(80.0, org.assertj.core.data.Offset.offset(0.1));
    }

    @Test
    void recordSession_whenNewScoreIsBest_shouldUpdateBestScore() {
        progress.recordSession(70, 300);
        progress.recordSession(95, 300);

        assertThat(progress.getBestScore()).isEqualTo(95);
    }

    @Test
    void recordSession_whenNewScoreIsLower_shouldKeepPreviousBest() {
        progress.recordSession(90, 300);
        progress.recordSession(50, 300);

        assertThat(progress.getBestScore()).isEqualTo(90);
    }

    @Test
    void recordSession_streakIncrementsWithEachSession() {
        progress.recordSession(75, 300);
        progress.recordSession(80, 300);
        progress.recordSession(85, 300);

        assertThat(progress.getCurrentStreak()).isEqualTo(3);
    }

    @Test
    void recordSession_whenDurationIsZero_shouldCountAsZeroMinutes() {
        progress.recordSession(70, 0);

        assertThat(progress.getTotalMinutes()).isEqualTo(0);
        assertThat(progress.getTotalSessions()).isEqualTo(1);
    }
}
