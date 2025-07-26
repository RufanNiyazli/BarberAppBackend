package com.project.barberreservation.service;

import com.project.barberreservation.dto.AuthResponse;
import com.project.barberreservation.entity.RefreshToken;
import com.project.barberreservation.entity.User;

public interface IAuthService {
    public AuthResponse register(User user);

    public AuthResponse login(User user);

    public AuthResponse refreshAccessToken(RefreshToken tokenStr);
}
