package com.asdeveloper.BloodMate.UsersHealth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HealthRepository extends JpaRepository<Model, Long> {
    Model findByUser(com.asdeveloper.BloodMate.Users.User user);
}
