package com.asdeveloper.BloodMate.Camps;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampRepository extends JpaRepository<Camps, Long>{
    @Query("SELECT c FROM Camps c WHERE " +
            "(LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')) OR " +
            "LOWER(c.location) LIKE LOWER(CONCAT('%', :location, '%')) OR " +
            "c.organizerBy = :organizerBy) AND" +
            "(:city IS NULL OR LOWER(c.city) LIKE LOWER(CONCAT('%', :city, '%')))" +
            "ORDER BY c.date DESC")
    List<Camps> findCampsByFilters(
        @Param("title") String title,
        @Param("location") String location,
        @Param("city") String city,
        @Param("organizerBy") String organizerBy
    );

}
