package com.project.barberreservation.entity;

import com.project.barberreservation.enumtype.ServiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer durationMinutes;

    private Double price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "services")
    private List<Appointment> appointments;


}
