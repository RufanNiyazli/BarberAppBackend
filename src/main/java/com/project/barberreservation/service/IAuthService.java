package com.project.barberreservation.service;

import com.project.barberreservation.dto.request.LoginRequest;
import com.project.barberreservation.dto.request.RefreshTokenRequest;
import com.project.barberreservation.dto.request.RegisterRequest;
import com.project.barberreservation.dto.request.VerifyUserRequest;
import com.project.barberreservation.dto.response.AuthResponse;


public interface IAuthService {
    public AuthResponse register(RegisterRequest registerRequest);

    public AuthResponse login(LoginRequest loginRequest);

    public AuthResponse refreshAccessToken(RefreshTokenRequest tokenStr);

    public void verifyUser(VerifyUserRequest verifyUserRequest);

    public void resendVerificationCode(String email);

}
