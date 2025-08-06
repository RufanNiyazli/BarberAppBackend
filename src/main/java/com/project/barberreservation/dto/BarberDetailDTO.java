package com.project.barberreservation.dto;

import com.project.barberreservation.enumtype.GenderType;
import com.project.barberreservation.enumtype.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberDetailDTO {
    private Long id;
    private String name;
    private String photoUrl;
    private String location;

    private Double rating;
    private GenderType targetGender;

    private List<ServiceDTO> services;
    private List<ReviewDTO> reviews;

    private List<ScheduleDto> schedules;
}
