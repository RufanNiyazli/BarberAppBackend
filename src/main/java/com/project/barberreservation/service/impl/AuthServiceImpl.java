package com.project.barberreservation.service.impl;

import com.project.barberreservation.dto.AuthResponse;
import com.project.barberreservation.entity.RefreshToken;
import com.project.barberreservation.entity.User;
import com.project.barberreservation.repository.RefreshTokenRepository;
import com.project.barberreservation.repository.UserRepository;
import com.project.barberreservation.security.JwtService;
import com.project.barberreservation.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor


public class AuthServiceImpl implements IAuthService {
    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenImpl refreshTokenService;

    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthResponse register(User requestUser) {
        if (userRepository.existsByEmail(requestUser.getEmail())) {
            throw new RuntimeException("this email exist");
        }
        User dbUser = userRepository.save(requestUser);
        String accessToken = jwtService.generateToken(dbUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(dbUser);
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessToken, refreshToken.getToken(), dbUser.getRole());
    }

    @Override
    public AuthResponse login(User loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public AuthResponse refreshAccessToken(RefreshToken tokenStr) {
        return null;
    }
}
