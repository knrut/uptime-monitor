package com.example.uptime.api.dto;
import jakarta.validation.constraints.*;

public record TargetUpdateRequest(
        @NotBlank String name,
        @NotBlank @Size(max = 2024) @Pattern(regexp = "https?://.*")
        String url,
        @NotNull Boolean enabled,
        @Min(10) @Max(86400) Integer checkEverysec
) {}
