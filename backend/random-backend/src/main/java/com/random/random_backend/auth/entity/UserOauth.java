package com.random.random_backend.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_oauth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserOauth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Provider provider;

    @Column(name = "provider_id", nullable = false, length = 255)
    private String providerId;

    @Builder
    public UserOauth(User user, Provider provider, String providerId) {
        this.user = user;
        this.provider = provider;
        this.providerId = providerId;
    }

    public enum Provider {
        GOOGLE
    }
}
