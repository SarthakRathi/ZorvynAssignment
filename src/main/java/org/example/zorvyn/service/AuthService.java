package org.example.zorvyn.service;

import lombok.RequiredArgsConstructor;
import org.example.zorvyn.dto.request.RegisterRequestDTO;
import org.example.zorvyn.entity.User;
import org.example.zorvyn.entity.UserStatus;
import org.example.zorvyn.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(RegisterRequestDTO request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);
        return "User registered successfully!";
    }
}