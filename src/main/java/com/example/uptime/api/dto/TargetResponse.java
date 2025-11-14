package com.example.uptime.api.dto;

public record TargetResponse(
        Long id, String name, String url, Boolean enabled, Integer checkEverySec
) {}
