package com.asdeveloper.bloodmate.Models;


public class BloodRequests {
    private Long id;
    private String patientName;
    private String hospitalName;
    private String location;
    private String city;
    private String bloodGroup;
    private String urgencyLevel;
    private String description;
    private String note;
    private Boolean isDonationCompleted;
    private Long deadline;
    private Long requestDate;
    private User requester;
    private User donor;

    public BloodRequests() {
    }

    public BloodRequests(Long id, String patientName, String hospitalName, String location, String city, String bloodGroup, String urgencyLevel, String description, String note, Boolean isDonationCompleted, Long deadline, Long requestDate, User requester, User donor) {
        this.id = id;
        this.patientName = patientName;
        this.hospitalName = hospitalName;
        this.location = location;
        this.city = city;
        this.bloodGroup = bloodGroup;
        this.urgencyLevel = urgencyLevel;
        this.description = description;
        this.note = note;
        this.isDonationCompleted = isDonationCompleted;
        this.deadline = deadline;
        this.requestDate = requestDate;
        this.requester = requester;
        this.donor = donor;
    }

    public Long getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getDeadline() {
        return deadline;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

    public Long getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Long requestDate) {
        this.requestDate = requestDate;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getDonor() {
        return donor;
    }

    public void setDonor(User donor) {
        this.donor = donor;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getDonationCompleted() {
        return isDonationCompleted;
    }

    public void setDonationCompleted(Boolean donationCompleted) {
        isDonationCompleted = donationCompleted;
    }
}
