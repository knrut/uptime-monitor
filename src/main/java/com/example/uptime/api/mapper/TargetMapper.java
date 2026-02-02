package com.example.uptime.api.mapper;
import com.example.uptime.api.dto.TargetResponse;
import com.example.uptime.domain.Target;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TargetMapper {

    @Mapping(target = "id",            expression = "java(t.getId())")
    @Mapping(target = "name",          expression = "java(t.getName())")
    @Mapping(target = "url",           expression = "java(t.getUrl())")
    @Mapping(target = "enabled",       expression = "java(t.getEnabled())")
    @Mapping(target = "checkEverySec", expression = "java(t.getCheckEverySec())")
    TargetResponse toResponse(Target t);
}