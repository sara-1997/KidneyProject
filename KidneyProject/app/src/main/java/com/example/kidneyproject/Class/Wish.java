package com.example.kidneyproject.Class;

public class Wish {
    String VolunteerId , PatientId, WishTitle , Description , Place ,Status,Key;

    double Latitude ;
    double Longitude;

    public Wish(String volunteerId, String patientId, String wishTitle, String description, String place, String status, String key, double latitude, double longitude) {
        VolunteerId = volunteerId;
        PatientId = patientId;
        WishTitle = wishTitle;
        Description = description;
        Place = place;
        Status = status;
        Key = key;
        Latitude = latitude;
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getStatus() {
        return Status;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }


    public void setStatus(String status) {
        Status = status;
    }

    public Wish() {

    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getVolunteerId() {
        return VolunteerId;
    }

    public void setVolunteerId(String volunteerId) {
        VolunteerId = volunteerId;
    }

    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
    }

    public String getWishTitle() {
        return WishTitle;
    }

    public void setWishTitle(String wishTitle) {
        WishTitle = wishTitle;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
