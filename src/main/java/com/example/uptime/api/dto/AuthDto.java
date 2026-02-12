package com.example.uptime.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class AuthDto {
    public record RegisterRequest(
            @NotBlank @Size(min = 3, max = 64) String username,
            @NotBlank @Size(min = 6, max = 128) String password
    ) {}

    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {}

    public record MeResponse(Long id, String username) {}

    public record ChangePasswordRequest(
            @NotBlank @Size(min = 6, max = 128) String oldPassword,
            @NotBlank @Size(min = 6, max = 128) String newPassword
    ) {}
}
