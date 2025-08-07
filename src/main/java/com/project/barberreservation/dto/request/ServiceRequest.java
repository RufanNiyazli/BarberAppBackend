package com.project.barberreservation.dto.request;


import com.project.barberreservation.enumtype.ServiceType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequest {
    private ServiceType serviceType;
    private String description;
    private Integer durationMinutes;
    private Double price;
}
