package com.project.barberreservation.repository;

import com.project.barberreservation.entity.Master;
import com.project.barberreservation.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByIdAndMaster(Long id, Master master);

}
