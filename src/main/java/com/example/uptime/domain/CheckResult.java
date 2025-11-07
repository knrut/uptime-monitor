package com.example.uptime.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.time.OffsetDateTime;

@Entity
@Table(name = "check_result", indexes = {
        @Index(name = "idx_check_result_target_created", columnList = "target_id, created_at DESC")
})
@Getter @Setter @Accessors(chain = true)
public class CheckResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_id", nullable = false)
    private Target target;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckStatus status;

    @Column(name = "latency_ms", nullable = false)
    private Integer latencyMs;

    @Column(name = "erros_msg")
    private String errorMsg;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdAt = OffsetDateTime.now();
}
