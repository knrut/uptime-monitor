package com.example.uptime.repo;

import com.example.uptime.domain.CheckResult;
import com.example.uptime.domain.Target;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckResultRepository extends JpaRepository<CheckResult, Long> {
    Page<CheckResult> findByTarget(Target target, Pageable pageable);
    Optional<CheckResult> findTopByTargetOrderByCreatedAtDesc(Target target);
}
