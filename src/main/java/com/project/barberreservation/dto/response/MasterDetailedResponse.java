package com.project.barberreservation.dto.response;

import com.project.barberreservation.enumtype.GenderType;
import com.project.barberreservation.enumtype.MasterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterDetailedResponse {
    private Long id;
    private String name;
    private String profilePhotoUrl;
    private List<String> galleryPhotos;
    private String location;

    private Boolean isProfileComplete;

    private Double rating;
    private GenderType targetGender;
    private Boolean is_available;
    private MasterType masterType;

    private List<ServiceResponse> services;
    private List<ReviewResponse> reviews;
    private List<ScheduleResponse> schedules;
}
