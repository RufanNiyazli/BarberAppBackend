package com.project.barberreservation.dto.response;

import com.project.barberreservation.enumtype.GenderType;
import com.project.barberreservation.enumtype.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberResponse {
    private Long id;
    private String name;
    private String profilePhotoUrl;
    private List<com.project.barberreservation.enumtype.ServiceType> serviceTypes;
    private Double rating;
    private GenderType targetGender;
    private String location;
}
