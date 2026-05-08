package com.thropic.talki.gamification.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserStreakTest {

    private UserStreak streak;

    @BeforeEach
    void setUp() {
        streak = new UserStreak("user-manuel-001");
    }

    @Test
    void recordSession_whenFirstSession_shouldStartStreakAtOne() {
        streak.recordSession(75);

        assertThat(streak.getCurrentStreak()).isEqualTo(1);
        assertThat(streak.getLongestStreak()).isEqualTo(1);
        assertThat(streak.getTotalXp()).isEqualTo(75);
    }

    @Test
    void recordSession_whenConsecutiveDays_shouldIncrementStreak() {
        streak.setLastSessionDate(LocalDate.now().minusDays(1));
        streak.setCurrentStreak(3);
        streak.setLongestStreak(3);

        streak.recordSession(80);

        assertThat(streak.getCurrentStreak()).isEqualTo(4);
        assertThat(streak.getLongestStreak()).isEqualTo(4);
    }

    @Test
    void recordSession_whenSessionOnSameDay_shouldKeepCurrentStreak() {
        streak.setLastSessionDate(LocalDate.now());
        streak.setCurrentStreak(5);
        streak.setLongestStreak(5);

        streak.recordSession(90);

        assertThat(streak.getCurrentStreak()).isEqualTo(5);
    }

    @Test
    void recordSession_whenGapInDays_shouldResetStreakToOne() {
        streak.setLastSessionDate(LocalDate.now().minusDays(3));
        streak.setCurrentStreak(10);
        streak.setLongestStreak(10);

        streak.recordSession(60);

        assertThat(streak.getCurrentStreak()).isEqualTo(1);
        assertThat(streak.getLongestStreak()).isEqualTo(10);
    }

    @Test
    void recordSession_xpAccumulatesAcrossSessions() {
        streak.recordSession(80);
        streak.recordSession(70);
        streak.recordSession(90);

        assertThat(streak.getTotalXp()).isEqualTo(240);
    }

    @Test
    void recordSession_whenCurrentStreakExceedsLongest_shouldUpdateLongest() {
        streak.setLastSessionDate(LocalDate.now().minusDays(1));
        streak.setCurrentStreak(7);
        streak.setLongestStreak(7);

        streak.recordSession(85);

        assertThat(streak.getLongestStreak()).isEqualTo(8);
    }
}
