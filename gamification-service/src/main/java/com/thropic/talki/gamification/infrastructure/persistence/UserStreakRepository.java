package com.thropic.talki.gamification.infrastructure.persistence;

import com.thropic.talki.gamification.domain.model.UserStreak;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStreakRepository extends JpaRepository<UserStreak, Long> {
    Optional<UserStreak> findByUserId(String userId);
    List<UserStreak> findTop10ByOrderByTotalXpDesc();
}
