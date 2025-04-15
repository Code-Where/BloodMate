package com.asdeveloper.BloodMate.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>{
    @Query("Select u from User u where u.phone = :phone")
    User findByNumber(@Param("phone") String phone);
}
