package com.thropic.talki.progress.infrastructure.persistence;

import com.thropic.talki.progress.domain.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUserId(String userId);
}
