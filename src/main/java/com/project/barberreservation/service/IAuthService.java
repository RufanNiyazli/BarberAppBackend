package com.project.barberreservation.service;

import com.project.barberreservation.dto.authDto.*;


public interface IAuthService {
    public AuthResponse register(RegisterRequest registerRequest);

    public AuthResponse login(LoginRequest loginRequest);

    public AuthResponse refreshAccessToken(RefreshTokenRequest tokenStr);

    public void verifyUser(VerifyUserDto verifyUserDto);

    public void resendVerificationCode(String email);

}
