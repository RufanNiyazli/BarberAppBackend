package com.project.barberreservation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.barberreservation.dto.response.*;
import com.project.barberreservation.entity.Master;
import com.project.barberreservation.entity.User;
import com.project.barberreservation.enumtype.ServiceType;
import com.project.barberreservation.repository.MasterRepository;
import com.project.barberreservation.repository.UserRepository;
import com.project.barberreservation.service.IMasterService;
import com.project.barberreservation.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class MasterServiceImpl implements IMasterService {
    private final MasterRepository masterRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final FileStorageUtil fileStorageUtil;


    @Override
    public List<MasterDetailedResponse> readAllBarbers() {
        List<Master> optionals = masterRepository.findAll();


        return optionals.stream()
                .map(this::convertToBarberResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MasterDetailedResponse readBarberById(Long id) {
        Optional<Master> optional = masterRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("This barber notFound!");
        }
        Master master = optional.get();
        List<ServiceResponse> serviceResponses = master.getServices().stream()
                .map(service -> ServiceResponse.builder()
                        .id(service.getId())
                        .serviceType(service.getServiceType())
                        .price(service.getPrice())
                        .durationMinutes(service.getDurationMinutes())
                        .build())
                .toList();

        List<ReviewResponse> reviewResponses = master.getReviews().stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .barberName(master.getName())
                        .createdAt(review.getCreatedAt())
                        .comment(review.getComment())
                        .customerName(review.getCustomer().getUsername())
                        .rating(review.getRating())

                        .build()
                ).toList();
        List<ScheduleResponse> scheduleResponses = master.getSchedules().stream()
                .map(
                        schedule -> ScheduleResponse.builder()
                                .endTime(schedule.getEndTime())
                                .startTime(schedule.getStartTime())
                                .dayOfWeek(schedule.getDayOfWeek())
                                .build()
                ).toList();


        return MasterDetailedResponse.builder()
                .id(master.getId())
                .services(serviceResponses)
                .rating(master.getRating())
                .name(master.getName())
                .reviews(reviewResponses)
                .schedules(scheduleResponses)
                .services(serviceResponses)
                .targetGender(master.getTargetGender())
                .location(master.getLocation())
                .build();
    }

    @Override
    public MasterDetailedResponse readBarberProfileForOwnProfile() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Master> optional = masterRepository.findByUserId(user.getId());
        if (optional.isEmpty()) {
            throw new RuntimeException("Barber not found!");
        }
        Master master = optional.get();

        List<ServiceResponse> serviceResponses = master.getServices().stream()
                .map(service -> ServiceResponse.builder()
                        .id(service.getId())
                        .serviceType(service.getServiceType())
                        .price(service.getPrice())
                        .durationMinutes(service.getDurationMinutes())
                        .build())
                .toList();

        List<ReviewResponse> reviewResponses = master.getReviews().stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .barberName(master.getName())
                        .createdAt(review.getCreatedAt())
                        .comment(review.getComment())
                        .customerName(review.getCustomer().getUsername())
                        .rating(review.getRating())

                        .build()
                ).toList();
        List<ScheduleResponse> scheduleResponses = master.getSchedules().stream()
                .map(
                        schedule -> ScheduleResponse.builder()
                                .endTime(schedule.getEndTime())
                                .startTime(schedule.getStartTime())
                                .dayOfWeek(schedule.getDayOfWeek())
                                .build()
                ).toList();

        return MasterDetailedResponse.builder()
                .id(master.getId())
                .services(serviceResponses)
                .rating(master.getRating())
                .targetGender(master.getTargetGender())
                .schedules(scheduleResponses)
                .location(master.getLocation())
                .name(master.getName())
                .reviews(reviewResponses)


                .build();
    }

    @Override
    public MasterDetailedResponse updateBarberProfile(Map<String, Object> updates,
                                                      MultipartFile profilePhoto,
                                                      MultipartFile[] galleryPhotos) throws IOException {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Master master = masterRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Barber not found!"));


        if (updates != null) {
            objectMapper.updateValue(master, updates);
        }
        if (!profilePhoto.isEmpty()) {
            String profilePhotoUrl = fileStorageUtil.saveProfilePhoto(profilePhoto);
            master.setProfilePhotoUrl(profilePhotoUrl);

        }
        if (galleryPhotos != null) {
            for (MultipartFile file : galleryPhotos) {
                if (!file.isEmpty()) {
                    String url = fileStorageUtil.saveGalleryPhotos(file);
                    master.getGalleryPhotos().add(url);
                }
            }

        }

        master.setUpdatedAt(LocalDateTime.now());
        Master dbMaster = masterRepository.save(master);

        //
        List<ServiceResponse> serviceResponses = dbMaster.getServices().stream()
                .map(service -> ServiceResponse.builder()
                        .id(service.getId())
                        .serviceType(service.getServiceType())
                        .price(service.getPrice())
                        .durationMinutes(service.getDurationMinutes())
                        .build())
                .toList();

        List<ReviewResponse> reviewResponses = dbMaster.getReviews().stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .barberName(master.getName())
                        .createdAt(review.getCreatedAt())
                        .comment(review.getComment())
                        .customerName(review.getCustomer().getUsername())
                        .rating(review.getRating())

                        .build()
                ).toList();
        List<ScheduleResponse> scheduleResponses = dbMaster.getSchedules().stream()
                .map(
                        schedule -> ScheduleResponse.builder()
                                .endTime(schedule.getEndTime())
                                .startTime(schedule.getStartTime())
                                .dayOfWeek(schedule.getDayOfWeek())
                                .build()
                ).toList();

        return MasterDetailedResponse.builder()
                .id(dbMaster.getId())
                .services(serviceResponses)
                .rating(dbMaster.getRating())
                .name(dbMaster.getName())
                .profilePhotoUrl(dbMaster.getProfilePhotoUrl())
                .galleryPhotos(dbMaster.getGalleryPhotos())
                .targetGender(dbMaster.getTargetGender())
                .schedules(scheduleResponses)
                .reviews(reviewResponses)
                .location(dbMaster.getLocation())
                .is_available(dbMaster.getIs_available())

                .build();
    }


    private MasterDetailedResponse convertToBarberResponse(Master master) {

        List<ServiceType> serviceTypes = master.getServices()
                .stream().map(com.project.barberreservation.entity.Service::getServiceType)
                .distinct().toList();
        return MasterDetailedResponse.builder()
                .id(master.getId())
                .name(master.getName())
                .profilePhotoUrl(master.getProfilePhotoUrl())
                .serviceTypes(serviceTypes)
                .rating(master.getRating())
                .targetGender(master.getTargetGender())
                .location(master.getLocation())
                .build();
    }
}
