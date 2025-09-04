package com.project.barberreservation.entity;

import com.project.barberreservation.enumtype.GenderType;
import com.project.barberreservation.enumtype.MasterType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Master {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;
    private String profilePhotoUrl;
    @ElementCollection
    @CollectionTable(name = "master_gallery", joinColumns = @JoinColumn(name = "master_id"))
    @Column(name = "photo_url")
    @Builder.Default
    private List<String> galleryPhotos = new ArrayList<>();

    private MasterType masterType;

    private String location;

    private Double rating;

    @Enumerated(EnumType.STRING)
    private GenderType targetGender;

    private Boolean isProfileComplete = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean is_available = false;

    @OneToMany(mappedBy = "master")
    private List<Service> services;

    @OneToMany(mappedBy = "master")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "master")
    private List<Review> reviews;

    @OneToMany(mappedBy = "master")
    private List<Schedule> schedules;
}
