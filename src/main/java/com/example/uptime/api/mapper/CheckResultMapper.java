package com.example.uptime.api.mapper;

import com.example.uptime.api.dto.CheckResultResponse;
import com.example.uptime.domain.CheckResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
//
//@Mapper(
//        componentModel = "spring",
//        unmappedTargetPolicy = ReportingPolicy.ERROR
//)
//public interface CheckResultMapper {
//    @Mapping(target = "targetId", source = "target.id")
//    CheckResultResponse toResponse(CheckResult r);
//}

@Mapper(componentModel = "spring")
public interface CheckResultMapper {

    @Mapping(target = "id",        expression = "java(r.getId())")
    @Mapping(target = "targetId",  expression = "java(r.getTarget().getId())")
    @Mapping(target = "status",    expression = "java(r.getStatus())")
    @Mapping(target = "latencyMs", expression = "java(r.getLatencyMs())")
    @Mapping(target = "errorMsg",  expression = "java(r.getErrorMsg())")
    @Mapping(target = "createdAt", expression = "java(r.getCreatedAt())")
    CheckResultResponse toResponse(CheckResult r);
}


