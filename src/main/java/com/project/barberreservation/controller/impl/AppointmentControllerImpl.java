package com.project.barberreservation.controller.impl;

import com.project.barberreservation.controller.IAppointmentController;
import com.project.barberreservation.dto.request.AppointmentRequest;
import com.project.barberreservation.dto.request.AppointmentUpdateRequest;
import com.project.barberreservation.dto.response.AppointmentResponse;
import com.project.barberreservation.service.IAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppointmentControllerImpl implements IAppointmentController {
    private final IAppointmentService appointmentService;

    @PostMapping({"/customer/create-appointment", "/barber/create-appointment"})
    @Override
    public AppointmentResponse createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        return appointmentService.createAppointment(appointmentRequest);
    }

    @Override
    @GetMapping("/customer/read-appointments")
    public List<AppointmentResponse> readAppointmentForCustomer() {
        return appointmentService.readAppointmentForCustomer();
    }

    @Override
    @PutMapping("/customer/update-appointment/{id}")
    public AppointmentResponse updateAppointment(@PathVariable(name = "id") Long appointmentId, @RequestBody AppointmentUpdateRequest updateRequest) {
        return appointmentService.updateAppointment(appointmentId, updateRequest);
    }

    @Override
    @DeleteMapping("/customer/delete-appointment/{id}")
    public void deleteAppointment(@PathVariable(name = "id") Long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);

    }
}
