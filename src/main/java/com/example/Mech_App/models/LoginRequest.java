package com.example.Mech_App.models;

import lombok.Data;

@Data
public class LoginRequest {
    private String role;   // ADMIN | CLIENT
    private String email;  // for ADMIN
    private String password;  // for ADMIN
    private String phone;  // for CLIENT
}
