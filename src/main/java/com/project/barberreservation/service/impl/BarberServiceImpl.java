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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
                .photoUrl(barber.getPhotoUrl())
                .reviews(reviewResponses)
                .schedules(scheduleResponses)
                .services(serviceResponses)
                .targetGender(barber.getTargetGender())
                .location(barber.getLocation())
                .build();
    }

    @Override
    public BarberDetailedResponse readBarberProfileForBarbers() {

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
                .photoUrl(barber.getPhotoUrl())
                .reviews(reviewResponses)


                .build();
    }

    @Override
    public BarberDetailedResponse updateBarberProfile(Map<String, Object> updates) throws JsonMappingException {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Barber> optional = barberRepository.findByUserId(user.getId());
        if (optional.isEmpty()) {
            throw new RuntimeException("Barber not found!");
        }
        Barber barber = optional.get();


        objectMapper.updateValue(barber, updates);

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
                .targetGender(dbBarber.getTargetGender())
                .schedules(scheduleResponses)
                .reviews(reviewResponses)
                .location(dbBarber.getLocation())
                .photoUrl(dbBarber.getPhotoUrl())

                .build();
    }


    private BarberResponse convertToBarberResponse(Barber barber) {

        List<ServiceType> serviceTypes = barber.getServices()
                .stream().map(com.project.barberreservation.entity.Service::getServiceType)
                .distinct().toList();
        return BarberResponse.builder()
                .id(barber.getId())
                .name(barber.getName())
                .photoUrl(barber.getPhotoUrl())
                .serviceTypes(serviceTypes)
                .rating(barber.getRating())
                .targetGender(barber.getTargetGender())
                .location(barber.getLocation())
                .build();
    }
}
