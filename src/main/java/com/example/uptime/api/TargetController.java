package com.example.uptime.api;

import com.example.uptime.api.dto.*;
import com.example.uptime.api.mapper.Mappers;
import com.example.uptime.domain.Target;
import com.example.uptime.repo.TargetRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/targets")
@RequiredArgsConstructor
public class TargetController {

    private final TargetRepository targets;

    @GetMapping
    public Page<TargetResponse> list(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size) {
        Page<Target> p = targets.findAll(PageRequest.of(page, Math.min(size, 100), Sort.by("id").ascending()));
        return p.map(Mappers::toResponse);
    }

    @GetMapping("/{id}")
    public TargetResponse get(@PathVariable Long id) {
        Target t = targets.findById(id).orElseThrow(() -> new NotFoundException("Target %d not found".formatted(id)));
        return Mappers.toResponse(t);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<TargetResponse> create(@RequestBody @Valid TargetCreateRequest req) {
        if (targets.existsByUrl(req.url())) {
            throw new ConflictException("Target with url '%s' already exists".formatted(req.url()));
        }
        Target t = new Target()
                .setName(req.name())
                .setUrl(req.url())
                .setEnabled(req.enabled())
                .setCheckEverySec(req.checkEverySec());
        Target saved = targets.save(t);
        return ResponseEntity.created(URI.create("/api/targets/" + saved.getId()))
                .body(Mappers.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Transactional
    public TargetResponse update(@PathVariable Long id, @RequestBody @Valid TargetUpdateRequest req) {
        Target t = targets.findById(id).orElseThrow(() -> new NotFoundException("Target %d not found".formatted(id)));

        if (!t.getUrl().equals(req.url()) && targets.existsByUrl(req.url())) {
            throw new ConflictException("Target with url '%s' already exists".formatted(req.url()));
        }

        t.setName(req.name())
                .setUrl(req.url())
                .setEnabled(req.enabled())
                .setCheckEverySec(req.checkEverySec());

        return Mappers.toResponse(t);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Target t = targets.findById(id).orElseThrow(() -> new NotFoundException("Target %d not found".formatted(id)));
        targets.delete(t);
        return ResponseEntity.noContent().build();
    }
}
