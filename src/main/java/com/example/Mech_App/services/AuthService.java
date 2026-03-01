package com.example.Mech_App.services;

import com.example.Mech_App.models.AuthResponse;
import com.example.Mech_App.models.LoginRequest;
import com.example.Mech_App.models.RefreshRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshRequest request);
}
