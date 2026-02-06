package com.example.uptime.api.dto;

import jakarta.validation.constraints.*;

public record TargetCreateRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 2048, message = "Name must be at most 2048 characters")
        String name,

        @NotBlank(message = "URL is required")
        @Pattern(regexp = "https?://.*", message = "URL must start with http:// or https://")
        String url,

        @NotNull
        Boolean enabled,

        @Min(value = 10, message = "Check interval must be at least 10 seconds")
        @Max(value = 86400, message = "Check interval must be at most 86400 seconds")
        Integer checkEverySec
) {}






