package com.thropic.talki.gamification.domain.event;

import java.time.Instant;

public class ScoringCompletedEvent {
    private String eventId;
    private String eventType;
    private Instant occurredAt;
    private String sessionId;
    private String userId;
    private int overallScore;

    public ScoringCompletedEvent() {}

    public String getEventId() { return eventId; }
    public void setEventId(String e) { this.eventId = e; }
    public String getEventType() { return eventType; }
    public void setEventType(String e) { this.eventType = e; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant o) { this.occurredAt = o; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String s) { this.sessionId = s; }
    public String getUserId() { return userId; }
    public void setUserId(String u) { this.userId = u; }
    public int getOverallScore() { return overallScore; }
    public void setOverallScore(int o) { this.overallScore = o; }
}
