package com.random.random_backend.domain.auth.service;

import com.random.random_backend.domain.auth.dto.SignUpRequest;
import com.random.random_backend.domain.auth.entity.UserCredential;
import com.random.random_backend.domain.auth.repository.UserCredentialRepository;
import com.random.random_backend.domain.user.entity.User;
import com.random.random_backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signup(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.builder().email(signUpRequest.getEmail()).build();
        userRepository.save(user);

        UserCredential userCredential = UserCredential.builder().user(user).password(passwordEncoder.encode(signUpRequest.getPassword())).build();
        userCredentialRepository.save(userCredential);

        return user.getId();
    }
}
