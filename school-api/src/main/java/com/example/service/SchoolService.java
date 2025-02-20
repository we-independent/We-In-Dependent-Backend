package com.example.service;

import com.example.models.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolService extends JpaRepository<School, Long> {

    // Custom query to find schools within a given radius (using Haversine formula or a simplified version)
    @Query("SELECT s FROM School s WHERE " +
            "POWER(s.latitude - :latitude, 2) + POWER(s.longitude - :longitude, 2) <= :radius * :radius")
    List<School> getNearbySchools(double latitude, double longitude, double radius);
}

