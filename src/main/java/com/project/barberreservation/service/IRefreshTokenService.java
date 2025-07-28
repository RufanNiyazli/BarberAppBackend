package com.project.barberreservation.service;

import com.project.barberreservation.dto.RefreshTokenRequest;
import com.project.barberreservation.entity.RefreshToken;
import com.project.barberreservation.entity.User;

public interface IRefreshTokenService {
    public RefreshToken createRefreshToken(User user);
    public RefreshToken validateRefreshToken(RefreshTokenRequest refreshToken);
}
