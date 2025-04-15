package com.asdeveloper.BloodMate.UsersHealth;

import jakarta.persistence.Entity;
import com.asdeveloper.BloodMate.Users.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "userhealth")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private boolean hasDiabetes;

    private boolean hasHypertension;

    private boolean hasAsthma;

    private boolean hasThyroid;

    private boolean hasHeartDisease;

    private Integer weight;

    private boolean anyRecentSurgery;

    private boolean willDonate;
}
