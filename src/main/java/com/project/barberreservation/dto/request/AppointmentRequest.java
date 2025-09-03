package com.project.barberreservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRequest {

    private Long masterId;
    private List<Long> serviceIds;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
}
