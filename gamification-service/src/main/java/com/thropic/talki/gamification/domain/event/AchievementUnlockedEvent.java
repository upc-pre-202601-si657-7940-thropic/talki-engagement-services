package com.thropic.talki.gamification.domain.event;

import java.time.Instant;
import java.util.UUID;

public class AchievementUnlockedEvent {
    private String eventId;
    private String eventType = "achievement.unlocked";
    private Instant occurredAt;
    private String userId;
    private String achievementId;
    private String achievementName;
    private String description;

    public AchievementUnlockedEvent() {}

    public AchievementUnlockedEvent(String userId, String achievementId,
                                     String achievementName, String description) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = Instant.now();
        this.userId = userId;
        this.achievementId = achievementId;
        this.achievementName = achievementName;
        this.description = description;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String e) { this.eventId = e; }
    public String getEventType() { return eventType; }
    public void setEventType(String e) { this.eventType = e; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant o) { this.occurredAt = o; }
    public String getUserId() { return userId; }
    public void setUserId(String u) { this.userId = u; }
    public String getAchievementId() { return achievementId; }
    public void setAchievementId(String a) { this.achievementId = a; }
    public String getAchievementName() { return achievementName; }
    public void setAchievementName(String a) { this.achievementName = a; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
}
