package com.example.uptime.api.dto;

import jakarta.validation.constraints.*;

public record TargetCreateRequest(
        @NotBlank String name,
        @NotBlank @Size(max = 2048) @Pattern(regexp = "https?://.*", message = "URL musi zaczynać się od http(s)://")
        String url,
        @NotNull Boolean enabled,
        @Min(10) @Max(86400) Integer checkEverySec
) {}






