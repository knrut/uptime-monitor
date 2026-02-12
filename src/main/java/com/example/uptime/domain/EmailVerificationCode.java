package com.example.uptime.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Entity
@Table(name = "email_verification_code", indexes = {
        @Index(name = "idx_verif_user_id", columnList = "user_id"),
        @Index(name = "idx_verif_expires_at", columnList = "expiresAt")
})
@Getter @Setter @Accessors(chain = true)
public class EmailVerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 10)
    private String code;

    @Column(name="expires_at", nullable=false)
    private Instant expiresAt;

    @Column(name="created_at", nullable=false)
    private Instant createdAt = Instant.now();

}
