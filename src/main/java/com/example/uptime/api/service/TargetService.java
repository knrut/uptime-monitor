package com.example.uptime.api.service;


import com.example.uptime.api.ConflictException;
import com.example.uptime.api.NotFoundException;
import com.example.uptime.api.dto.TargetCreateRequest;
import com.example.uptime.api.dto.TargetResponse;
import com.example.uptime.api.dto.TargetUpdateRequest;
import com.example.uptime.api.mapper.TargetMapper;
import com.example.uptime.domain.Target;
import com.example.uptime.repo.TargetRepository;
import com.example.uptime.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TargetService {
    private final TargetRepository targetRepository;
    private final UserRepository userRepository;
    private final TargetMapper targetMapper;

    public Page<TargetResponse> findAll(String username, int page, int size) {
        Page<Target> targets = targetRepository.findAllByUserUsername(
                username,
                PageRequest.of(page, Math.min(size, 100), Sort.by(Sort.Direction.DESC, "id"))
        );
        return targets.map(targetMapper::toResponse);
    }

    public TargetResponse findById(String username, Long id) {
        Target target = targetRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new NotFoundException("Target not found"));
        return targetMapper.toResponse(target);
    }

    public TargetResponse save(String username, TargetCreateRequest targetCreateRequest) {
        if (targetRepository.existsByUserUsernameAndUrl(username, targetCreateRequest.url())) {
            throw new ConflictException("Target URL already exists");
        }

        com.example.uptime.domain.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Target target = new Target()
                .setName(targetCreateRequest.name())
                .setUrl(targetCreateRequest.url())
                .setEnabled(targetCreateRequest.enabled())
                .setCheckEverySec(targetCreateRequest.checkEverySec())
                .setUser(user);

        Target savedTarget = targetRepository.save(target);
        return targetMapper.toResponse(savedTarget);
    }

    public TargetResponse update(String username, Long id, TargetUpdateRequest targetUpdateRequest) {
        Target target = targetRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new NotFoundException("Target not found"));

        target.setName(targetUpdateRequest.name())
                .setCheckEverySec(targetUpdateRequest.checkEverySec());

        Target savedTarget = targetRepository.save(target);
        return targetMapper.toResponse(savedTarget);
    }

    public void delete(String username, Long id) {
        Target target = targetRepository.findByIdAndUserUsername(id, username)
                    .orElseThrow(() -> new NotFoundException("Target not found"));
        targetRepository.delete(target);
    }

    public void freeze(String username, Long id) {
        Target target = targetRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new NotFoundException("Target not found"));
        target.setEnabled(false);
        targetRepository.save(target);
    }

    public void unfreeze(String username, Long id) {
        Target target = targetRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new NotFoundException("Target not found"));
        target.setEnabled(true);
        targetRepository.save(target);
    }
}
