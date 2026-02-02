package com.example.uptime.api.service;

import com.example.uptime.api.NotFoundException;
import com.example.uptime.api.dto.CheckResultResponse;
import com.example.uptime.api.dto.CreateCheckResultRequest;
import com.example.uptime.api.mapper.CheckResultMapper;
import com.example.uptime.api.mapper.Mappers;
import com.example.uptime.domain.CheckResult;
import com.example.uptime.domain.Target;
import com.example.uptime.repo.CheckResultRepository;
import com.example.uptime.repo.TargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CheckResultService {
    private final TargetRepository targets;
    private final CheckResultRepository results;
    private final CheckResultMapper checkResultMapper;

    public Page<CheckResultResponse> findAll(Long targetId, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(Sort.Direction.DESC, "createdAt"));
        if (targetId == null) {
            return results.findAll(pageable).map(checkResultMapper::toResponse);
        }
        Target t = targets.findById(targetId)
                .orElseThrow(() -> new NotFoundException("Target %d not found".formatted(targetId)));
        return results.findByTarget(t, pageable).map(checkResultMapper::toResponse);
    }

    public CheckResultResponse findById(Long id) {
        CheckResult r = results.findById(id).orElseThrow(() -> new NotFoundException("Result %d not found".formatted(id)));
        return checkResultMapper.toResponse(r);
    }

    public CheckResultResponse save(CreateCheckResultRequest req) {
        Target t = targets.findById(req.targetId())
                .orElseThrow(() -> new NotFoundException("Target %d not found".formatted(req.targetId())));

        CheckResult r = new CheckResult()
                .setTarget(t)
                .setStatus(req.status())
                .setLatencyMs(req.latencyMs() != null ? req.latencyMs() : 0)
                .setErrorMsg(req.errorMsg())
                .setCreatedAt(req.createdAt() != null ? req.createdAt() : OffsetDateTime.now());

        CheckResult saved = results.save(r);
        return checkResultMapper.toResponse(saved);
    }

}
