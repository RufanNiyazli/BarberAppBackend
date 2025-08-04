package com.project.barberreservation.dto.authDto;

import com.project.barberreservation.enumtype.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    String accessToken;
    String refreshToken;
    RoleType roleType;

}
