package com.project.barberreservation.repository;

import com.project.barberreservation.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.master.id = :masterId")
    Double findAverageRatingByMasterId(@Param("masterId") Long masterId);

}
