package org.example.zorvyn.controller;

import lombok.RequiredArgsConstructor;
import org.example.zorvyn.dto.request.RegisterRequestDTO;
import org.example.zorvyn.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }
}