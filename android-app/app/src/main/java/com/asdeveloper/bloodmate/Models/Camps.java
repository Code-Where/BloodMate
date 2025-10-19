package com.asdeveloper.bloodmate.Models;

public class Camps {
    private Long id;

    private String title;
    private String location;
    private String city;
    private Long date;

    private String organizerBy;

    public Camps() {
    }

    public Camps(Long id, String title, String location, String city, Long date, String organizerBy) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.city = city;
        this.date = date;
        this.organizerBy = organizerBy;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getOrganizerBy() {
        return organizerBy;
    }

    public void setOrganizerBy(String organizerBy) {
        this.organizerBy = organizerBy;
    }
}
