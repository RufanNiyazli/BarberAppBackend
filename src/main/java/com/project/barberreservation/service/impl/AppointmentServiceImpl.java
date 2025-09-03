package com.project.barberreservation.service.impl;

import com.project.barberreservation.dto.request.AppointmentRequest;
import com.project.barberreservation.dto.request.AppointmentUpdateRequest;
import com.project.barberreservation.dto.response.AppointmentResponse;
import com.project.barberreservation.dto.response.ServiceResponse;
import com.project.barberreservation.entity.Appointment;
import com.project.barberreservation.entity.Master;
import com.project.barberreservation.entity.User;
import com.project.barberreservation.enumtype.ReservationStatus;
import com.project.barberreservation.repository.AppointmentRepository;
import com.project.barberreservation.repository.MasterRepository;
import com.project.barberreservation.repository.ServiceRepository;
import com.project.barberreservation.repository.UserRepository;
import com.project.barberreservation.service.IAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements IAppointmentService {
    private final AppointmentRepository appointmentRepository;

    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final MasterRepository masterRepository;

    @Override
    public AppointmentResponse createAppointment(AppointmentRequest appointmentRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        List<com.project.barberreservation.entity.Service> services = serviceRepository.findAllById(appointmentRequest.getServiceIds());


        if (services.isEmpty()) {
            throw new RuntimeException("No valid services found for given IDs");
        }
        Integer totalDuration = services.stream()
                .filter(s -> s.getDurationMinutes() != null)
                .mapToInt(com.project.barberreservation.entity.Service::getDurationMinutes)
                .sum();
        LocalTime endTime = appointmentRequest.getAppointmentTime().plusMinutes(totalDuration);


        Master master = masterRepository.findById(appointmentRequest.getMasterId()).orElseThrow(() -> new RuntimeException("Master not found!"));


        Appointment appointment = Appointment.builder()
                .appointmentDate(appointmentRequest.getAppointmentDate())
                .appointmentTime(appointmentRequest.getAppointmentTime())
                .appointmentEndTime(endTime)
                .services(services)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .master(master)
                .customer(user)
                .status(ReservationStatus.CONFIRMED)

                .build();


        appointmentRepository.save(appointment);


        return mapToAppointmentResponse(appointment);
    }

    @Override
    public List<AppointmentResponse> readAppointmentForCustomer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Appointment> appointments = appointmentRepository.findByCustomerIdOrderByAppointmentDateDesc(user.getId());

        return appointments.stream()
                .map(this::mapToAppointmentResponse)
                .toList();
    }

    @Override
    public AppointmentResponse updateAppointment(Long appointmentId, AppointmentUpdateRequest updateRequest) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment Not Found"));

        if (updateRequest.getAppointmentDate() != null) {
            appointment.setAppointmentDate(updateRequest.getAppointmentDate());
        }

        if (updateRequest.getAppointmentTime() != null) {
            appointment.setAppointmentTime(updateRequest.getAppointmentTime());
        }

        if (updateRequest.getServiceIds() != null && !updateRequest.getServiceIds().isEmpty()) {
            List<com.project.barberreservation.entity.Service> services = serviceRepository.findAllById(updateRequest.getServiceIds());
            if (services.isEmpty()) {
                throw new RuntimeException("No valid services found");
            }
            appointment.setServices(services);
        }

        appointment.setUpdatedAt(LocalDateTime.now());
        appointmentRepository.save(appointment);

        return mapToAppointmentResponse(appointment);
    }


    @Override
    public void deleteAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
        appointmentRepository.delete(appointment);

    }

    private AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        List<ServiceResponse> services = appointment.getServices().stream().map(
                service -> ServiceResponse.builder()
                        .serviceType(service.getServiceType())
                        .price(service.getPrice())
                        .description(service.getDescription())
                        .durationMinutes(service.getDurationMinutes())
                        .id(service.getId())
                        .build()
        ).toList();

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .appointmentEndTime(appointment.getAppointmentEndTime())
                .masterName(appointment.getMaster().getName())
                .customerName(appointment.getCustomer().getUsername())
                .status(appointment.getStatus())
                .services(services)
                .build();


    }
}
