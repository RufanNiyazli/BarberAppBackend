package com.project.barberreservation.dto.response;

import com.project.barberreservation.enumtype.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentResponse {
    private Long id;
    private String barberName;
    private String customerName;
    private List<ServiceResponse> services;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private ReservationStatus status;
}
