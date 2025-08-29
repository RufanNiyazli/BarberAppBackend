package com.project.barberreservation.service.impl;

import com.project.barberreservation.dto.request.LoginRequest;
import com.project.barberreservation.dto.request.RefreshTokenRequest;
import com.project.barberreservation.dto.request.RegisterRequest;
import com.project.barberreservation.dto.request.VerifyUserRequest;
import com.project.barberreservation.dto.response.AuthResponse;
import com.project.barberreservation.entity.Barber;
import com.project.barberreservation.entity.RefreshToken;
import com.project.barberreservation.entity.User;
import com.project.barberreservation.enumtype.RoleType;
import com.project.barberreservation.repository.BarberRepository;
import com.project.barberreservation.repository.RefreshTokenRepository;
import com.project.barberreservation.repository.UserRepository;
import com.project.barberreservation.security.JwtService;
import com.project.barberreservation.service.IAuthService;
import com.project.barberreservation.service.IEmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenImpl refreshTokenService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final IEmailService emailService;
    private final BarberRepository barberRepository;

    @Override
    public AuthResponse register(RegisterRequest registerRequest) throws MessagingException {
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
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User dbUser = userRepository.save(user);

        if (dbUser.getRole().equals(RoleType.BARBER)) {
            Barber barber = Barber.builder()
                    .user(dbUser)
                    .name(dbUser.getUsername())
                    .rating(0.0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            barberRepository.save(barber);
        }

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);

        sendVerificationEmail(user);

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
        String newAccessToken = jwtService.generateToken(user);

        return new AuthResponse(newAccessToken, refreshToken.getToken(), user.getRole());
    }

    @Override
    public void verifyUser(VerifyUserRequest verifyUserRequest) {
        Optional<User> optionalUser = userRepository.findUserByEmail(verifyUserRequest.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(verifyUserRequest.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public void resendVerificationCode(String email) throws MessagingException {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));

            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private void sendVerificationEmail(User user) throws MessagingException {
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();

        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}