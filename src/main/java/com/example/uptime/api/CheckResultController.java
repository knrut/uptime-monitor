package com.example.uptime.api;


import com.example.uptime.api.dto.CheckResultResponse;
import com.example.uptime.api.dto.CreateCheckResultRequest;
import com.example.uptime.api.mapper.Mappers;
import com.example.uptime.domain.CheckResult;
import com.example.uptime.domain.Target;
import com.example.uptime.repo.CheckResultRepository;
import com.example.uptime.repo.TargetRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/results")
public class CheckResultController {

    private final CheckResultRepository results;
    private final TargetRepository targets;

    public CheckResultController(CheckResultRepository results, TargetRepository targets) {
        this.results = results;
        this.targets = targets;
    }

    @GetMapping
    public Page<CheckResultResponse> list(@RequestParam(required = false) Long targetId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(Sort.Direction.DESC, "createdAt"));
        if (targetId == null) {
            return results.findAll(pageable).map(Mappers::toResponse);
        }
        Target t = targets.findById(targetId)
                .orElseThrow(() -> new NotFoundException("Target %d not found".formatted(targetId)));
        return results.findByTarget(t, pageable).map(Mappers::toResponse);
    }

    @GetMapping("/{id}")
    public CheckResultResponse get(@PathVariable Long id) {
        CheckResult r = results.findById(id).orElseThrow(() -> new NotFoundException("Result %d not found".formatted(id)));
        return Mappers.toResponse(r);
    }

    // end point do recznego dodania wyniku (przyda sie zanim wjedzei scheduler)
    @PostMapping
    @Transactional
    public ResponseEntity<CheckResultResponse> create(@RequestBody @Valid CreateCheckResultRequest req) {
        Target t = targets.findById(req.targetId())
                .orElseThrow(() -> new NotFoundException("Target %d not found".formatted(req.targetId())));

        CheckResult r = new CheckResult()
                .setTarget(t)
                .setStatus(req.status())
                .setLatencyMs(req.latencyMs() != null ? req.latencyMs() : 0)
                .setErrorMsg(req.errorMsg())
                .setCreatedAt(req.createdAt() != null ? req.createdAt() : OffsetDateTime.now());

        CheckResult saved = results.save(r);
        return ResponseEntity.created(URI.create("/api/results/" + saved.getId()))
                .body(Mappers.toResponse(saved));
    }
}
