package com.asdeveloper.BloodMate.BloodRequests;

import java.util.Calendar;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.asdeveloper.BloodMate.Users.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "BloodRequests")
public class BloodRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String patientName;
    private String hospitalName;
    private String location;
    private String bloodGroup;
    private String city;
    private String urgencyLevel;
    private String description;
    
    private Boolean isDonationCompleted;
    private String note;
    private Long deadline;
    private Long requestDate  = Calendar.getInstance().getTimeInMillis();
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = true)
    private User donor;
}
