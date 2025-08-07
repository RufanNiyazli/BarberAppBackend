package com.project.barberreservation.dto.request;

import com.project.barberreservation.entity.Barber;
import com.project.barberreservation.entity.Service;
import com.project.barberreservation.entity.User;
import com.project.barberreservation.enumtype.ReservationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRequest {

    private Long barberId;
    private List<Long> serviceIds;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
}
