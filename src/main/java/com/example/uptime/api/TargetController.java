package com.example.uptime.api;

import com.example.uptime.api.dto.*;
import com.example.uptime.api.service.TargetService;
import com.example.uptime.repo.TargetRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
@RestController
@RequestMapping("/api/targets")
@RequiredArgsConstructor
public class TargetController {

    private final TargetService service;

    @GetMapping
    public Page<TargetResponse> list(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size,
                                     Authentication authentication) {
        return service.findAll(authentication.getName(), page, size);
    }

    @GetMapping("/{id}")
    public TargetResponse get(@PathVariable Long id, Authentication authentication) {
        return service.findById(authentication.getName(), id);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<TargetResponse> create(@RequestBody @Valid TargetCreateRequest req,
                                                 Authentication authentication) {
        TargetResponse saved = service.save(authentication.getName(), req);
        return ResponseEntity.created(URI.create("/api/targets/" + saved.id())).body(saved);
    }

    @PutMapping("/{id}")
    @Transactional
    public TargetResponse update(@PathVariable Long id, @RequestBody @Valid TargetUpdateRequest req,
                                 Authentication authentication) {
        TargetResponse targetResponse = service.update(authentication.getName(), id, req);
        return targetResponse;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        service.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/freeze")
    @Transactional
    public ResponseEntity<Void> freeze(@PathVariable Long id, Authentication authentication) {
        service.freeze(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/unfreeze")
    @Transactional
    public ResponseEntity<Void> unfreeze(@PathVariable Long id, Authentication authentication) {
        service.unfreeze(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
