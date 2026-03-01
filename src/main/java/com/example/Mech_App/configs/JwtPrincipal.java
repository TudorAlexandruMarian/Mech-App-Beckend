package com.example.Mech_App.configs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtPrincipal {
    private Long userId;
    private Long clientId;
    private String role;
}
