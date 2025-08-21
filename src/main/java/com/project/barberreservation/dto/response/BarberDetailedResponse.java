package com.project.barberreservation.dto.response;

import com.project.barberreservation.enumtype.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberDetailedResponse {
    private Long id;
    private String name;
    private String profilePhotoUrl;
    private List<String> galleryPhotos;
    private String location;

    private Double rating;
    private GenderType targetGender;

    private List<ServiceResponse> services;
    private List<ReviewResponse> reviews;
    private List<ScheduleResponse> schedules;
    private Boolean is_available;
}
