package com.example.Mech_App.controllers;

import com.example.Mech_App.bo.User;
import com.example.Mech_App.configs.CustomResponseStatusException;
import com.example.Mech_App.enums.UserRole;
import com.example.Mech_App.models.AdminCreateRequest;
import com.example.Mech_App.models.AuthResponse;
import com.example.Mech_App.models.LoginRequest;
import com.example.Mech_App.models.RefreshRequest;
import com.example.Mech_App.repositories.UserRepository;
import com.example.Mech_App.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${api-key}")
    private String configuredApiKey;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/admin")
    public ResponseEntity<String> createAdminUser(
            @RequestHeader(name = "X-API-KEY", required = true) String apiKey,
            @RequestBody AdminCreateRequest request
    ) {
        if (apiKey == null || !apiKey.equals(configuredApiKey)) {
            throw new CustomResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API key.");
        }

        String email = request.getEmail();
        String password = request.getPassword();

        if (email == null || email.isBlank()) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required.");
        }
        if (password == null || password.isBlank()) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new CustomResponseStatusException(HttpStatus.CONFLICT, "A user with this email already exists.");
        }

        User user = new User();
        user.setEmail(email.trim());
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(UserRole.ADMIN);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Admin user created successfully.");
    }
}
