package com.project.barberreservation.repository;

import com.project.barberreservation.entity.Barber;
import com.project.barberreservation.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByIdAndBarber(Long id, Barber barber);

}
