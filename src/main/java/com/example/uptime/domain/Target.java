package com.example.uptime.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "target", indexes = {
        @Index(name = "idx_target_url_unique", columnList = "url", unique = true)
})
@Getter @Setter @Accessors(chain = true)
public class Target {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(name = "check_every_sec", nullable = false)
    private Integer checkEverySec = 30;
}
