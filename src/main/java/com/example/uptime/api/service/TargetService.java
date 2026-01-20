package com.example.uptime.api.service;


import com.example.uptime.api.dto.TargetResponse;
import com.example.uptime.api.mapper.Mappers;
import com.example.uptime.domain.Target;
import com.example.uptime.repo.TargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TargetService {
    private final TargetRepository targets;

    public Page<TargetResponse> readAll(int page, int size) {
        Page<Target> p = targets.findAll(PageRequest.of(page, Math.min(size, 100), Sort.by("id").ascending()));
        return p.map(Mappers::toResponse);
    }
}
