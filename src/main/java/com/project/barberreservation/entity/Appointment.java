package com.project.barberreservation.entity;

import com.project.barberreservation.enumtype.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "appointment")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;

    @ManyToMany
    @JoinTable(
            name = "appointment_services",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private LocalTime appointmentEndTime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
