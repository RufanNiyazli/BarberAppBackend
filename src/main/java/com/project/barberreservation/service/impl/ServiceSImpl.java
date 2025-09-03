package com.project.barberreservation.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.barberreservation.dto.request.ServiceRequest;
import com.project.barberreservation.dto.response.ServiceResponse;
import com.project.barberreservation.entity.Master;
import com.project.barberreservation.entity.User;
import com.project.barberreservation.repository.MasterRepository;
import com.project.barberreservation.repository.ServiceRepository;
import com.project.barberreservation.repository.UserRepository;
import com.project.barberreservation.service.IServiceS;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceSImpl implements IServiceS {
    private final MasterRepository masterRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;


    @Override
    public ServiceResponse createService(ServiceRequest serviceRequest) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Master> optional = masterRepository.findByUserId(user.getId());
        if (optional.isEmpty()) {
            throw new RuntimeException("Master not found!");
        }
        Master master = optional.get();
        com.project.barberreservation.entity.Service service = com.project.barberreservation.entity.Service.builder()
                .createdAt(LocalDateTime.now())
                .serviceType(serviceRequest.getServiceType())
                .price(serviceRequest.getPrice())
                .updatedAt(LocalDateTime.now())
                .durationMinutes(serviceRequest.getDurationMinutes())
                .master(master)
                .description(serviceRequest.getDescription())
                .build();

        com.project.barberreservation.entity.Service savedService = serviceRepository.save(service);


        return ServiceResponse.builder()
                .id(savedService.getId())
                .serviceType(savedService.getServiceType())
                .description(savedService.getDescription())
                .durationMinutes(savedService.getDurationMinutes())
                .price(savedService.getPrice())
                .build();
    }

    @Override
    public ServiceResponse updateService(Map<String, Object> updates, Long id) throws JsonMappingException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Master> optional = masterRepository.findByUserId(user.getId());
        if (optional.isEmpty()) {
            throw new RuntimeException("Master not found!");
        }
        Master master = optional.get();

        Optional<com.project.barberreservation.entity.Service> optionalService = serviceRepository.findServiceByMasterAndId(master, id);
        if (optionalService.isEmpty()) {
            throw new RuntimeException("Service tapila bilmedi!");

        }
        com.project.barberreservation.entity.Service service = optionalService.get();
        objectMapper.updateValue(service, updates);
        service.setUpdatedAt(LocalDateTime.now());
        com.project.barberreservation.entity.Service dbService = serviceRepository.save(service);


        return ServiceResponse.builder()
                .price(dbService.getPrice())
                .description(dbService.getDescription())
                .serviceType(dbService.getServiceType())
                .id(service.getId())
                .durationMinutes(dbService.getDurationMinutes())
                .build();

    }

    @Override
    public void deleteService(Long id) {
        Optional<com.project.barberreservation.entity.Service> optional = serviceRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Service not Found! Can't deleted");

        }
        com.project.barberreservation.entity.Service service = optional.get();
        serviceRepository.delete(service);

    }

    @Override
    public ServiceResponse readService(Long serviceID) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Master> optional = masterRepository.findByUserId(user.getId());
        if (optional.isEmpty()) {
            throw new RuntimeException("Master not found!");
        }
        Master master = optional.get();

        Optional<com.project.barberreservation.entity.Service> optionalService = serviceRepository.findServiceByMasterAndId(master, serviceID);
        if (optionalService.isEmpty()) {
            throw new RuntimeException("Service notFound!");

        }
        com.project.barberreservation.entity.Service service = optionalService.get();

        return ServiceResponse.builder()
                .durationMinutes(service.getDurationMinutes())
                .serviceType(service.getServiceType())
                .price(service.getPrice())
                .description(service.getDescription())
                .id(service.getId())
                .build();
    }

    @Override
    public List<ServiceResponse> readServices() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Master> optional = masterRepository.findByUserId(user.getId());
        if (optional.isEmpty()) {
            throw new RuntimeException("Master not found!");
        }
        Master master = optional.get();

        List<com.project.barberreservation.entity.Service> services = serviceRepository.findServiceByMaster(master).orElseThrow(() -> new RuntimeException("Services not Found!"));
        return services.stream()
                .map(service -> ServiceResponse.builder()
                        .id(service.getId())
                        .serviceType(service.getServiceType())
                        .description(service.getDescription())
                        .durationMinutes(service.getDurationMinutes())
                        .price(service.getPrice())
                        .build())
                .toList();
    }
}
