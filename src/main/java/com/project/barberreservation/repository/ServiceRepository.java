package com.project.barberreservation.repository;

import com.project.barberreservation.entity.Barber;
import com.project.barberreservation.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findServiceByBarberAndId(Barber barber, Long id);

    Optional<List<Service>> findServiceByBarber(Barber barber);


}
