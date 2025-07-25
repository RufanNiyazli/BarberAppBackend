package com.project.barberreservation.entity;

import com.project.barberreservation.enumtype.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Barber barber;

    @ManyToOne
    private Service service;

    private LocalDateTime reservationTime;
    private ReservationStatus status;
}
