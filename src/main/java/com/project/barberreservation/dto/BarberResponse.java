package com.project.barberreservation.dto;

import com.project.barberreservation.enumtype.GenderType;
import com.project.barberreservation.enumtype.SpecializationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberResponse {
    private Long id;
    private String name;
    private String photoUrl;
    private Set<SpecializationType> specializations;
    private Double rating;
    private GenderType targetGender;
}
