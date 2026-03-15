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

    /**
     * For client login: use plain phone only. User stores "phone-identifier", so we must look up Client by phone, not by User.phoneNumber.
     */
    private static String normalizeClientPhone(String code) {
        if (code == null || code.isBlank()) return code;
        String t = code.trim();
        int dash = t.indexOf('-');
        return dash >= 0 ? t.substring(0, dash).trim() : t;
    }

    private static String identifierFromCode(String code) {
        if (code == null || code.isBlank()) return null;
        String t = code.trim();
        int dash = t.indexOf('-');
        return dash >= 0 ? t.substring(dash + 1).trim() : null;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String role = normalize(request.getRole());
        if ("ADMIN".equalsIgnoreCase(role)) {
            return loginAdmin(normalize(request.getEmail()), request.getPassword());
        }
        if ("CLIENT".equalsIgnoreCase(role)) {
            return loginClient(request.getPhone());
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

        String normalizeClientPhone = normalizeClientPhone(phone);
        String identifier = identifierFromCode(phone);
        // New rules: client sends plain phone only. User.phoneNumber stores "phone-identifier", so we must not look up User by phone.
        // Lookup: Client by phone -> User by clientId.
        Client client = clientRepository.findByPhoneNumber(normalizeClientPhone)
                .orElseThrow(() -> new CustomResponseStatusException(HttpStatus.UNAUTHORIZED, "No account found for this phone number."));

        if(!client.getIdentifier().equals(identifier)){
           throw  new CustomResponseStatusException(HttpStatus.UNAUTHORIZED, "No account identifier found for this phone number.");
        }
        User user = userRepository.findByClientId(client.getId())
                .orElseThrow(() -> new CustomResponseStatusException(HttpStatus.UNAUTHORIZED, "No account found for this phone number."));
        return buildAuthResponse(user, client);
    }

    private AuthResponse buildAuthResponse(User user, Client client) {
        String subject = user.getEmail() != null ? user.getEmail() : (client != null ? client.getPhoneNumber() : user.getPhoneNumber());
        String accessToken = jwtUtil.generateAccessToken(
                user.getId(),
                subject,
                user.getRole(),
                user.getClientId()
        );
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        String name = client != null ? client.getName() : (user.getEmail() != null ? user.getEmail().split("@")[0] : user.getPhoneNumber());
        // For CLIENT, return plain phone (not "phone-identifier") for display
        String phone = client != null ? client.getPhoneNumber() : user.getPhoneNumber();
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole().name())
                .userId(user.getId())
                .clientId(user.getClientId())
                .name(name)
                .email(user.getEmail())
                .phone(phone)
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
