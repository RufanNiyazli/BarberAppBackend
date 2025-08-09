package com.project.barberreservation.controller;

import com.project.barberreservation.dto.request.AppointmentRequest;
import com.project.barberreservation.dto.request.AppointmentUpdateRequest;
import com.project.barberreservation.dto.response.AppointmentResponse;

import java.util.List;

public interface IAppointmentController {
    public AppointmentResponse createAppointment(AppointmentRequest appointmentRequest);

    public List<AppointmentResponse> readAppointmentForCustomer();

    public AppointmentResponse updateAppointment(Long appointmentId, AppointmentUpdateRequest updateRequest);

    public void deleteAppointment(Long appointmentId);
}
