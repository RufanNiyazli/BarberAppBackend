package com.project.barberreservation.service.impl;

import com.project.barberreservation.dto.AuthResponse;
import com.project.barberreservation.dto.LoginRequest;
import com.project.barberreservation.dto.RefreshTokenRequest;
import com.project.barberreservation.dto.RegisterRequest;
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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

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
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("this email exist");
        }
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .gender(registerRequest.getGender())
                .phoneNumber(registerRequest.getPhoneNumber())
                .profilePicture(registerRequest.getProfilePicture())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User dbUser = userRepository.save(user);
        String accessToken = jwtService.generateToken(dbUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(dbUser);
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessToken, refreshToken.getToken(), dbUser.getRole());
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        Optional<User> optionalUser = userRepository.findUserByEmail(loginRequest.getEmail());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found!");
        }
        String accessToken = jwtService.generateToken(optionalUser.get());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(optionalUser.get());
        refreshTokenRepository.save(refreshToken);
        return new AuthResponse(accessToken, refreshToken.getToken(), optionalUser.get().getRole());
    }

    @Override
    public AuthResponse refreshAccessToken(RefreshTokenRequest tokenStr) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(tokenStr);
        User user = refreshToken.getUser();
        String newAccesstoken = jwtService.generateToken(user);


        return new AuthResponse(newAccesstoken, refreshToken.getToken(), user.getRole());
    }
}
