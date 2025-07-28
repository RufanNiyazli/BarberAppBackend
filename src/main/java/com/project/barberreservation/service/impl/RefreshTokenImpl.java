package com.project.barberreservation.service.impl;

import com.project.barberreservation.dto.RefreshTokenRequest;
import com.project.barberreservation.entity.RefreshToken;
import com.project.barberreservation.entity.User;
import com.project.barberreservation.repository.RefreshTokenRepository;
import com.project.barberreservation.service.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenImpl implements IRefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setCreatedAt(new Date());
        refreshToken.setExpiredAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        refreshToken.setRevoked(false);
        return refreshToken;
    }

    @Override
    public RefreshToken validateRefreshToken(RefreshTokenRequest refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token tapılmadı!"));


        if (token.getExpiredAt().before(new Date())) {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            throw new RuntimeException("Refresh token vaxtı keçib. Yenidən giriş edin.");
        }

        if (token.isRevoked()) {
            throw new RuntimeException("Refresh token artıq istifadə olunub və bloklanıb.");
        }

        return token;
    }
}
