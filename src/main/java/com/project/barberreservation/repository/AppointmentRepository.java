package com.project.barberreservation.repository;

import com.project.barberreservation.entity.Appointment;
import com.project.barberreservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByCustomerIdOrderByAppointmentDateDesc(Long customerId);
}
