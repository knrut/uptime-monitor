package com.example.uptime.api.dto;

import com.example.uptime.domain.CheckStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public record CreateCheckResultRequest(
        @NotNull Long targetId,
        @NotNull CheckStatus status,
        @PositiveOrZero Integer latencyMs,
        @Size(max = 4000) String errorMsg,
        OffsetDateTime createdAt
) {}
