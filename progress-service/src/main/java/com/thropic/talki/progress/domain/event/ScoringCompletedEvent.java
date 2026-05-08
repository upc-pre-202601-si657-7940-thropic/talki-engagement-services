package com.thropic.talki.progress.domain.event;

import java.time.Instant;

public class ScoringCompletedEvent {
    private String eventId;
    private String eventType;
    private Instant occurredAt;
    private String sessionId;
    private String userId;
    private int overallScore;
    private double silenceRatio;
    private double wordsPerMinute;
    private int durationSeconds;

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
    public double getSilenceRatio() { return silenceRatio; }
    public void setSilenceRatio(double s) { this.silenceRatio = s; }
    public double getWordsPerMinute() { return wordsPerMinute; }
    public void setWordsPerMinute(double w) { this.wordsPerMinute = w; }
    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int d) { this.durationSeconds = d; }
}
