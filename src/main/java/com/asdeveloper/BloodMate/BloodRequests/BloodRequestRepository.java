package com.asdeveloper.BloodMate.BloodRequests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long>{
    @Query("SELECT br FROM BloodRequest br WHERE " +
       "(:city IS NULL OR br.city = :city) AND " +
       "(:bloodGroup IS NULL OR TRIM(LOWER(br.bloodGroup)) = TRIM(LOWER(:bloodGroup))) AND " +
       "(:urgencyLevel IS NULL OR br.urgencyLevel = :urgencyLevel) AND " +
       "(:unassigned IS NULL OR (:unassigned = TRUE AND br.donor IS NULL)) AND " + 
       "(:requesterId IS NULL OR br.requester.id = :requesterId) AND " +
       "(:donorId IS NULL OR br.donor.id = :donorId) " +
       "ORDER BY br.requestDate DESC")
    List<BloodRequest> findByFilters(
       @Param("city") String city,
       @Param("bloodGroup") String bloodGroup,
       @Param("urgencyLevel") String urgencyLevel,
       @Param("requesterId") Long requesterId,
       @Param("donorId") Long donorId,
       @Param("unassigned") Boolean unassigned
    );

}