package com.thropic.talki.progress.interfaces.rest;

import com.thropic.talki.progress.domain.model.UserProgress;
import com.thropic.talki.progress.infrastructure.persistence.UserProgressRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/progress")
public class ProgressController {

    private final UserProgressRepository progressRepository;

    public ProgressController(UserProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(@RequestParam String userId) {
        UserProgress progress = progressRepository.findByUserId(userId)
                .orElse(new UserProgress(userId));

        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "totalSessions", progress.getTotalSessions(),
                "totalMinutes", progress.getTotalMinutes(),
                "averageScore", progress.getAverageScore(),
                "bestScore", progress.getBestScore(),
                "currentStreak", progress.getCurrentStreak()
        ));
    }
}
