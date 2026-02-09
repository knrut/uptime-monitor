package com.example.uptime.repo;
import com.example.uptime.domain.Target;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TargetRepository extends JpaRepository<Target, Long> {
    Page<Target> findAllByUserUsername(String username, Pageable pageable);
    Optional<Target> findByIdAndUserUsername(Long id, String username);
    boolean existsByUserUsernameAndUrl(String username, String url);
}
