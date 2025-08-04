package com.project.barberreservation.controller;

import com.project.barberreservation.dto.authDto.*;

public interface IAuthController {
    public AuthResponse register(RegisterRequest registerRequest);

    public AuthResponse login(LoginRequest loginRequest);

    public AuthResponse refreshAccessToken(RefreshTokenRequest tokenStr);

    public void verifyUser(VerifyUserDto verifyUserDto);

    public void resendVerificationCode(String email);
}
