package com.example.Mech_App.services.impl;

import com.example.Mech_App.bo.Client;
import com.example.Mech_App.bo.User;
import com.example.Mech_App.configs.CustomResponseStatusException;
import com.example.Mech_App.enums.UserRole;
import com.example.Mech_App.models.AuthResponse;
import com.example.Mech_App.models.LoginRequest;
import com.example.Mech_App.models.RefreshRequest;
import com.example.Mech_App.repositories.ClientRepository;
import com.example.Mech_App.repositories.UserRepository;
import com.example.Mech_App.services.AuthService;
import com.example.Mech_App.services.ServiceFactory;
import com.example.Mech_App.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ServiceFactory serviceFactory;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private static String normalize(String s) {
        return s == null ? null : s.trim();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String role = normalize(request.getRole());
        if ("ADMIN".equalsIgnoreCase(role)) {
            return loginAdmin(normalize(request.getEmail()), request.getPassword());
        }
        if ("CLIENT".equalsIgnoreCase(role)) {
            return loginClient(normalize(request.getPhone()));
        }
        throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role. Use ADMIN or CLIENT.");
    }

    private AuthResponse loginAdmin(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required for admin login.");
        }
        if (password == null || password.isBlank()) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required for admin login.");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password."));
        if (user.getRole() != UserRole.ADMIN) {
            throw new CustomResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }
        if (user.getPasswordHash() == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new CustomResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }
        return buildAuthResponse(user, null);
    }

    private AuthResponse loginClient(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is required for client login.");
        }
        User user = userRepository.findByPhoneNumberAndRole(phone, UserRole.CLIENT)
                .orElseThrow(() -> new CustomResponseStatusException(HttpStatus.UNAUTHORIZED, "No account found for this phone number."));
        Client client = null;
        if (user.getClientId() != null) {
            client = clientRepository.findById(user.getClientId()).orElse(null);
        }
        return buildAuthResponse(user, client);
    }

    private AuthResponse buildAuthResponse(User user, Client client) {
        String accessToken = jwtUtil.generateAccessToken(
                user.getId(),
                user.getEmail() != null ? user.getEmail() : user.getPhoneNumber(),
                user.getRole(),
                user.getClientId()
        );
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        String name = client != null ? client.getName() : (user.getEmail() != null ? user.getEmail().split("@")[0] : user.getPhoneNumber());
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole().name())
                .userId(user.getId())
                .clientId(user.getClientId())
                .name(name)
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .build();
    }

    @Override
    public AuthResponse refresh(RefreshRequest request) {
        if (request.getRefreshToken() == null || request.getRefreshToken().isBlank()) {
            throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is required.");
        }
        try {
            Claims claims = jwtUtil.parseToken(request.getRefreshToken());
            if (!jwtUtil.isRefreshToken(claims)) {
                throw new CustomResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token.");
            }
            Long userId = Long.parseLong(claims.getSubject());
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found."));
            Client client = null;
            if (user.getClientId() != null) {
                client = clientRepository.findById(user.getClientId()).orElse(null);
            }
            return buildAuthResponse(user, client);
        } catch (Exception e) {
            if (e instanceof CustomResponseStatusException) throw (CustomResponseStatusException) e;
            throw new CustomResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token.");
        }
    }
}
