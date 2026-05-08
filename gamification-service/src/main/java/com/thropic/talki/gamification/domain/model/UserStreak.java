package com.thropic.talki.gamification.domain.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_streaks")
public class UserStreak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    private int currentStreak;
    private int longestStreak;
    private int totalXp;
    private LocalDate lastSessionDate;

    public UserStreak() {}

    public UserStreak(String userId) {
        this.userId = userId;
    }

    public void recordSession(int score) {
        LocalDate today = LocalDate.now();
        if (lastSessionDate != null && lastSessionDate.plusDays(1).equals(today)) {
            currentStreak++;
        } else if (lastSessionDate == null || !lastSessionDate.equals(today)) {
            currentStreak = 1;
        }
        if (currentStreak > longestStreak) longestStreak = currentStreak;
        lastSessionDate = today;
        totalXp += score;
    }

    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public void setUserId(String u) { this.userId = u; }
    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int c) { this.currentStreak = c; }
    public int getLongestStreak() { return longestStreak; }
    public void setLongestStreak(int l) { this.longestStreak = l; }
    public int getTotalXp() { return totalXp; }
    public void setTotalXp(int t) { this.totalXp = t; }
    public LocalDate getLastSessionDate() { return lastSessionDate; }
    public void setLastSessionDate(LocalDate d) { this.lastSessionDate = d; }
}
