package com.asdeveloper.BloodMate.Notifications;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asdeveloper.BloodMate.Users.User;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long>{
    FcmToken findByUser(User user);
}
