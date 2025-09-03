package com.project.barberreservation.dto.response;

import com.project.barberreservation.enumtype.ReservationStatus;
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
public class AppointmentResponse {
    private Long id;
    private String masterName;
    private String customerName;
    private List<ServiceResponse> services;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalTime appointmentEndTime;
    private ReservationStatus status;
}
