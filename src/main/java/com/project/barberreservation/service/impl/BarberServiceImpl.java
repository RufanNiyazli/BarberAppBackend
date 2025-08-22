package com.project.barberreservation.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.barberreservation.dto.response.*;
import com.project.barberreservation.entity.Barber;
import com.project.barberreservation.entity.User;
import com.project.barberreservation.enumtype.ServiceType;
import com.project.barberreservation.repository.BarberRepository;
import com.project.barberreservation.repository.UserRepository;
import com.project.barberreservation.service.IBarberService;
import com.project.barberreservation.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarberServiceImpl implements IBarberService {
    private final BarberRepository barberRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final FileStorageUtil fileStorageUtil;


    @Override
    public List<BarberResponse> readAllBarbers() {
        List<Barber> optionals = barberRepository.findAll();


        return optionals.stream()
                .map(this::convertToBarberResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BarberDetailedResponse readBarberById(Long id) {
        Optional<Barber> optional = barberRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("This barber notFound!");
        }
        Barber barber = optional.get();
        List<ServiceResponse> serviceResponses = barber.getServices().stream()
                .map(service -> ServiceResponse.builder()
                        .id(service.getId())
                        .serviceType(service.getServiceType())
                        .price(service.getPrice())
                        .durationMinutes(service.getDurationMinutes())
                        .build())
                .toList();

        List<ReviewResponse> reviewResponses = barber.getReviews().stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .barberName(barber.getName())
                        .createdAt(review.getCreatedAt())
                        .comment(review.getComment())
                        .customerName(review.getCustomer().getUsername())
                        .rating(review.getRating())

                        .build()
                ).toList();
        List<ScheduleResponse> scheduleResponses = barber.getSchedules().stream()
                .map(
                        schedule -> ScheduleResponse.builder()
                                .endTime(schedule.getEndTime())
                                .startTime(schedule.getStartTime())
                                .dayOfWeek(schedule.getDayOfWeek())
                                .build()
                ).toList();


        return BarberDetailedResponse.builder()
                .id(barber.getId())
                .services(serviceResponses)
                .rating(barber.getRating())
                .name(barber.getName())
                .reviews(reviewResponses)
                .schedules(scheduleResponses)
                .services(serviceResponses)
                .targetGender(barber.getTargetGender())
                .location(barber.getLocation())
                .build();
    }

    @Override
    public BarberDetailedResponse readBarberProfileForOwnProfile() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Barber> optional = barberRepository.findByUserId(user.getId());
        if (optional.isEmpty()) {
            throw new RuntimeException("Barber not found!");
        }
        Barber barber = optional.get();

        List<ServiceResponse> serviceResponses = barber.getServices().stream()
                .map(service -> ServiceResponse.builder()
                        .id(service.getId())
                        .serviceType(service.getServiceType())
                        .price(service.getPrice())
                        .durationMinutes(service.getDurationMinutes())
                        .build())
                .toList();

        List<ReviewResponse> reviewResponses = barber.getReviews().stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .barberName(barber.getName())
                        .createdAt(review.getCreatedAt())
                        .comment(review.getComment())
                        .customerName(review.getCustomer().getUsername())
                        .rating(review.getRating())

                        .build()
                ).toList();
        List<ScheduleResponse> scheduleResponses = barber.getSchedules().stream()
                .map(
                        schedule -> ScheduleResponse.builder()
                                .endTime(schedule.getEndTime())
                                .startTime(schedule.getStartTime())
                                .dayOfWeek(schedule.getDayOfWeek())
                                .build()
                ).toList();

        return BarberDetailedResponse.builder()
                .id(barber.getId())
                .services(serviceResponses)
                .rating(barber.getRating())
                .targetGender(barber.getTargetGender())
                .schedules(scheduleResponses)
                .location(barber.getLocation())
                .name(barber.getName())
                .reviews(reviewResponses)


                .build();
    }

    @Override
    public BarberDetailedResponse updateBarberProfile(Map<String, Object> updates,
                                                      MultipartFile profilePhoto,
                                                      MultipartFile[] galleryPhotos) throws IOException {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Barber barber = barberRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Barber not found!"));


        if (updates != null) {
            objectMapper.updateValue(barber, updates);
        }
        if (!profilePhoto.isEmpty()) {
            String profilePhotoUrl = fileStorageUtil.saveProfilePhoto(profilePhoto);
            barber.setProfilePhotoUrl(profilePhotoUrl);

        }
        if (galleryPhotos != null) {
            for (MultipartFile file : galleryPhotos) {
                if (!file.isEmpty()) {
                    String url = fileStorageUtil.saveGalleryPhotos(file);
                    barber.getGalleryPhotos().add(url);
                }
            }

        }

        barber.setUpdatedAt(LocalDateTime.now());
        Barber dbBarber = barberRepository.save(barber);

        //
        List<ServiceResponse> serviceResponses = dbBarber.getServices().stream()
                .map(service -> ServiceResponse.builder()
                        .id(service.getId())
                        .serviceType(service.getServiceType())
                        .price(service.getPrice())
                        .durationMinutes(service.getDurationMinutes())
                        .build())
                .toList();

        List<ReviewResponse> reviewResponses = dbBarber.getReviews().stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .barberName(barber.getName())
                        .createdAt(review.getCreatedAt())
                        .comment(review.getComment())
                        .customerName(review.getCustomer().getUsername())
                        .rating(review.getRating())

                        .build()
                ).toList();
        List<ScheduleResponse> scheduleResponses = dbBarber.getSchedules().stream()
                .map(
                        schedule -> ScheduleResponse.builder()
                                .endTime(schedule.getEndTime())
                                .startTime(schedule.getStartTime())
                                .dayOfWeek(schedule.getDayOfWeek())
                                .build()
                ).toList();

        return BarberDetailedResponse.builder()
                .id(dbBarber.getId())
                .services(serviceResponses)
                .rating(dbBarber.getRating())
                .name(dbBarber.getName())
                .profilePhotoUrl(dbBarber.getProfilePhotoUrl())
                .galleryPhotos(dbBarber.getGalleryPhotos())
                .targetGender(dbBarber.getTargetGender())
                .schedules(scheduleResponses)
                .reviews(reviewResponses)
                .location(dbBarber.getLocation())
                .is_available(dbBarber.getIs_available())

                .build();
    }


    private BarberResponse convertToBarberResponse(Barber barber) {

        List<ServiceType> serviceTypes = barber.getServices()
                .stream().map(com.project.barberreservation.entity.Service::getServiceType)
                .distinct().toList();
        return BarberResponse.builder()
                .id(barber.getId())
                .name(barber.getName())
                .profilePhotoUrl(barber.getProfilePhotoUrl())
                .serviceTypes(serviceTypes)
                .rating(barber.getRating())
                .targetGender(barber.getTargetGender())
                .location(barber.getLocation())
                .build();
    }
}
