package com.project.barberreservation.controller;

import com.project.barberreservation.dto.AuthResponse;
import com.project.barberreservation.dto.LoginRequest;
import com.project.barberreservation.dto.RefreshTokenRequest;
import com.project.barberreservation.dto.RegisterRequest;

public interface IAuthController {
    public AuthResponse register(RegisterRequest registerRequest);

    public AuthResponse login(LoginRequest loginRequest);

    public AuthResponse refreshAccessToken(RefreshTokenRequest tokenStr);
}
