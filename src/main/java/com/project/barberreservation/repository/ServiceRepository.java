package com.project.barberreservation.repository;

import com.project.barberreservation.entity.Master;
import com.project.barberreservation.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findServiceByMasterAndId(Master master, Long id);

    Optional<List<Service>> findServiceByMaster(Master master);


}
