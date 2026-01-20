package com.example.uptime.api.service;


import com.example.uptime.api.ConflictException;
import com.example.uptime.api.NotFoundException;
import com.example.uptime.api.dto.TargetCreateRequest;
import com.example.uptime.api.dto.TargetResponse;
import com.example.uptime.api.dto.TargetUpdateRequest;
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

    public Page<TargetResponse> findAll(int page, int size) {
        Page<Target> p = targets.findAll(PageRequest.of(page, Math.min(size, 100), Sort.by("id").ascending()));
        return p.map(Mappers::toResponse);
    }

    public TargetResponse findById(Long id) {
        Target t = targets.findById(id).orElseThrow(() -> new NotFoundException("Target %d not found".formatted(id)));
        return Mappers.toResponse(t);
    }

    public TargetResponse save(TargetCreateRequest req) {
        if (targets.existsByUrl(req.url())) {
            throw new ConflictException("Target with '%s' already exists".formatted(req.url()));
        }

        Target t = new Target()
                .setName(req.name())
                .setUrl(req.url())
                .setEnabled(req.enabled())
                .setCheckEverySec(req.checkEverySec());

        Target saved = targets.save(t);
        return Mappers.toResponse(saved);
    }

    public TargetResponse update(Long id, TargetUpdateRequest req) {
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

    public void delete(Long id) {
        Target t = targets.findById(id).orElseThrow(() -> new NotFoundException("Target %d not found".formatted(id)));
        targets.delete(t);
    }
}
