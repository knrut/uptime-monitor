package com.example.uptime.repo;

import com.example.uptime.domain.EmailVerificationCode;
import com.example.uptime.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, Long> {
    void deleteByUser(User user);
    Optional<EmailVerificationCode> findTopByUserOrderByCreatedAtDesc(User user);
    Optional<EmailVerificationCode> findTopByUserAndExpiresAtAfterOrderByCreatedAtDesc(User user, Instant now);

}
