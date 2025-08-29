package com.project.barberreservation.controller;

import com.project.barberreservation.dto.request.LoginRequest;
import com.project.barberreservation.dto.request.RefreshTokenRequest;
import com.project.barberreservation.dto.request.RegisterRequest;
import com.project.barberreservation.dto.request.VerifyUserRequest;
import com.project.barberreservation.dto.response.AuthResponse;
import jakarta.mail.MessagingException;

public interface IAuthController {
    public AuthResponse register(RegisterRequest registerRequest) throws MessagingException;

    public AuthResponse login(LoginRequest loginRequest);

    public AuthResponse refreshAccessToken(RefreshTokenRequest tokenStr);

    public void verifyUser(VerifyUserRequest verifyUserRequest);

    public void resendVerificationCode(String email) throws MessagingException;
}
