package com.example.uptime.repo;

import com.example.uptime.domain.CheckResult;
import com.example.uptime.domain.CheckStatus;
import com.example.uptime.domain.Target;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CheckResultRepository extends JpaRepository<CheckResult, Long> {
    Page<CheckResult> findByTarget(Target target, Pageable pageable);
    Optional<CheckResult> findTopByTargetOrderByCreatedAtDesc(Target target);

    @Query("""
        select r from CheckResult r
        where r.target.id = :targetId
          and (:status is null or r.status = :status)
          and (:latencyMin is null or r.latencyMs >= :latencyMin)
          and (:latencyMax is null or r.latencyMs <= :latencyMax)
        """)
    Page<CheckResult> findByFilters(
            @Param("targetId") Long targetId,
            @Param("status") CheckStatus status,
            @Param("latencyMin") Integer latencyMin,
            @Param("latencyMax") Integer latencyMax,
            Pageable pageable
    );
}


