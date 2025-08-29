package com.project.barberreservation.controller.impl;

import com.project.barberreservation.controller.IAuthController;
import com.project.barberreservation.dto.request.LoginRequest;
import com.project.barberreservation.dto.request.RefreshTokenRequest;
import com.project.barberreservation.dto.request.RegisterRequest;
import com.project.barberreservation.dto.request.VerifyUserRequest;
import com.project.barberreservation.dto.response.AuthResponse;
import com.project.barberreservation.service.IAuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class AuthControllerImpl implements IAuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    @Override
    public AuthResponse register(@Valid @RequestBody RegisterRequest registerRequest) throws MessagingException {
        return authService.register(registerRequest);
    }

    @Override
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Override
    @PostMapping("/refresh-accessToken")
    public AuthResponse refreshAccessToken(@RequestBody RefreshTokenRequest tokenStr) {
        return authService.refreshAccessToken(tokenStr);
    }

    @PostMapping("/verify-user")
    @Override
    public void verifyUser(@RequestBody VerifyUserRequest verifyUserRequest) {
        authService.verifyUser(verifyUserRequest);
    }

    @PostMapping("/resend-code")
    @Override
    public void resendVerificationCode(@RequestBody String email) throws MessagingException {
        authService.resendVerificationCode(email);
    }
}