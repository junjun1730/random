package com.random.random_backend.domain.auth.repository;

import com.random.random_backend.domain.auth.entity.UserCredential;
import com.random.random_backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
    Optional<User> findByUserId(Long id);
}
