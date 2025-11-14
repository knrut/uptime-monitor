package com.example.uptime.api.dto;

import com.example.uptime.domain.CheckStatus;

import java.time.OffsetDateTime;

public record CheckResultResponse(
        Long id,
        Long targetId,
        CheckStatus status,
        Integer latencyMs,
        String errorMsg,
        OffsetDateTime createdAt
) {}
