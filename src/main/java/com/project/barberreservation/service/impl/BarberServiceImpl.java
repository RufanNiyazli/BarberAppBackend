package com.project.barberreservation.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.barberreservation.dto.*;
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
    public List<BarberResponse> getAllBarbers() {
        List<Barber> optionals = barberRepository.findAll();


        return optionals.stream()
                .map(this::convertToBarberResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BarberDetailDTO getBarberById(Long id) {
        Optional<Barber> optional = barberRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("This barber notFound!");
        }
        Barber barber = optional.get();
        List<ServiceDTO> serviceDTOs = barber.getServices().stream()
                .map(service -> ServiceDTO.builder()
                        .id(service.getId())
                        .serviceType(service.getServiceType())
                        .price(service.getPrice())
                        .durationMinutes(service.getDurationMinutes())
                        .build())
                .toList();

        List<ReviewDTO> reviewDTOS = barber.getReviews().stream()
                .map(review -> ReviewDTO.builder()
                        .id(review.getId())
                        .createdAt(review.getCreatedAt())
                        .comment(review.getComment())
                        .customerName(review.getCustomer().getUsername())
                        .rating(review.getRating())

                        .build()
                ).toList();
        List<ScheduleDto> scheduleDtos = barber.getSchedules().stream()
                .map(
                        schedule -> ScheduleDto.builder()
                                .endTime(schedule.getEndTime())
                                .startTime(schedule.getStartTime())
                                .dayOfWeek(schedule.getDayOfWeek())
                                .build()
                ).toList();


        return BarberDetailDTO.builder()
                .id(barber.getId())
                .services(serviceDTOs)
                .rating(barber.getRating())
                .name(barber.getName())
                .photoUrl(barber.getPhotoUrl())
                .reviews(reviewDTOS)
                .schedules(scheduleDtos)
                .services(serviceDTOs)
                .targetGender(barber.getTargetGender())
                .location(barber.getLocation())
                .build();
    }

    @Override
    public BarberDetailDTO updateBarberProfile(Map<String, Object> updates) throws JsonMappingException {

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
        List<ServiceDTO> serviceDTOs = dbBarber.getServices().stream()
                .map(service -> ServiceDTO.builder()
                        .id(service.getId())
                        .serviceType(service.getServiceType())
                        .price(service.getPrice())
                        .durationMinutes(service.getDurationMinutes())
                        .build())
                .toList();

        List<ReviewDTO> reviewDTOS = dbBarber.getReviews().stream()
                .map(review -> ReviewDTO.builder()
                        .id(review.getId())
                        .createdAt(review.getCreatedAt())
                        .comment(review.getComment())
                        .customerName(review.getCustomer().getUsername())
                        .rating(review.getRating())

                        .build()
                ).toList();
        List<ScheduleDto> scheduleDtos = dbBarber.getSchedules().stream()
                .map(
                        schedule -> ScheduleDto.builder()
                                .endTime(schedule.getEndTime())
                                .startTime(schedule.getStartTime())
                                .dayOfWeek(schedule.getDayOfWeek())
                                .build()
                ).toList();

        return BarberDetailDTO.builder()
                .id(dbBarber.getId())
                .services(serviceDTOs)
                .rating(dbBarber.getRating())
                .name(dbBarber.getName())
                .targetGender(dbBarber.getTargetGender())
                .schedules(scheduleDtos)
                .reviews(reviewDTOS)
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
