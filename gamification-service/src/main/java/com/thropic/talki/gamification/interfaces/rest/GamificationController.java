package com.thropic.talki.gamification.interfaces.rest;

import com.thropic.talki.gamification.domain.model.UserStreak;
import com.thropic.talki.gamification.infrastructure.persistence.UserStreakRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/gamification")
public class GamificationController {

    private final UserStreakRepository streakRepository;

    public GamificationController(UserStreakRepository streakRepository) {
        this.streakRepository = streakRepository;
    }

    @GetMapping("/streaks/{userId}")
    public ResponseEntity<Map<String, Object>> getStreaks(@PathVariable String userId) {
        UserStreak streak = streakRepository.findByUserId(userId)
                .orElse(new UserStreak(userId));

        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "currentStreak", streak.getCurrentStreak(),
                "longestStreak", streak.getLongestStreak(),
                "totalXp", streak.getTotalXp()
        ));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<Map<String, Object>>> getLeaderboard() {
        List<Map<String, Object>> leaderboard = streakRepository.findTop10ByOrderByTotalXpDesc()
                .stream()
                .map(s -> Map.<String, Object>of(
                        "userId", s.getUserId(),
                        "totalXp", s.getTotalXp(),
                        "currentStreak", s.getCurrentStreak()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(leaderboard);
    }
}
