package com.project.barberreservation.entity;

import com.project.barberreservation.enumtype.GenderType;
import com.project.barberreservation.enumtype.SpecializationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "barber")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Barber {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    @ElementCollection(targetClass = SpecializationType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "barber_specializations",
            joinColumns = @JoinColumn(name = "barber_id")
    )
    @Column(name = "specialization")
    private Set<SpecializationType> specializations;

    private String photoUrl;

    private Double rating;

    @Enumerated(EnumType.STRING)
    private GenderType targetGender;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "barber")
    private List<Service> services;

    @OneToMany(mappedBy = "barber")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "barber")
    private List<Review> reviews;

    @OneToMany(mappedBy = "barber")
    private List<Schedule> schedules;
}
