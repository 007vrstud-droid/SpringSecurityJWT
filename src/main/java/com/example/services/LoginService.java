package com.example.services;

import com.example.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private static final int MAX_FAILED_ATTEMPTS = 5;

    @Transactional
    public void loginFailed(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            int newFailCount = user.getFailedAttempts() + 1;
            user.setFailedAttempts(newFailCount);
            if (newFailCount >= MAX_FAILED_ATTEMPTS) {
                user.setAccountNonLocked(false);
            }
        });
    }

    @Transactional
    public void loginSucceeded(String username) {
        userRepository.findByUsername(username).ifPresent(user -> user.setFailedAttempts(0));
    }
}