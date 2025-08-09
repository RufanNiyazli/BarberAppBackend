package com.project.barberreservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentUpdateRequest {
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private List<Long> serviceIds;
}
