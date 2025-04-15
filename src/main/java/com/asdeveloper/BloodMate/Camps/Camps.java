package com.asdeveloper.BloodMate.Camps;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Camps")
public class Camps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String location;
    private String city;
    private Long date;
    
    @Column(name = "organizer_by")
    private String organizerBy;
}
