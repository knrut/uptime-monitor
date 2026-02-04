package com.example.uptime.api.dto;
import jakarta.validation.constraints.*;

public record TargetUpdateRequest(
        @NotBlank String name,
        @Min(10) @Max(86400) Integer checkEverySec
) {}

