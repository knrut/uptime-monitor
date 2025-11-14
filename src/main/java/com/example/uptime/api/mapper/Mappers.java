package com.example.uptime.api.mapper;

import com.example.uptime.api.dto.*;
import com.example.uptime.domain.CheckResult;
import com.example.uptime.domain.Target;

public class Mappers {
    private Mappers(){}

    public static TargetResponse toResponse(Target t) {
        return new TargetResponse(t.getId(), t.getName(), t.getUrl(), t.getEnabled(), t.getCheckEverySec());
    }

    public static CheckResultResponse toResponse(CheckResult r){
        return new CheckResultResponse(
                r.getId(),
                r.getTarget().getId(),
                r.getStatus(),
                r.getLatencyMs(),
                r.getErrorMsg(),
                r.getCreatedAt()
        );
    }
}
