package com.project.barberreservation.service.impl;

import com.project.barberreservation.dto.AuthResponse;
import com.project.barberreservation.entity.RefreshToken;
import com.project.barberreservation.entity.User;
import com.project.barberreservation.repository.UserRepository;
import com.project.barberreservation.security.JwtService;
import com.project.barberreservation.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor


public class AuthServiceImpl implements IAuthService {
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final UserRepository userRepository;



    @Override
    public AuthResponse register(User user) {
        return null;
    }

    @Override
    public AuthResponse login(User user) {
        return null;
    }

    @Override
    public AuthResponse refreshAccessToken(RefreshToken tokenStr) {
        return null;
    }
}
