package com.example.uptime.repo;
import com.example.uptime.domain.Target;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TargetRepository extends JpaRepository<Target, Long> {
    boolean existsByUrl(String url);
    Optional<Target> findByUrl(String url);
}
