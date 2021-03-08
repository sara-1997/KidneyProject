package com.example.kidneyproject.Class;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.Date;

public class visit {
    String Statues , Place , Description;
    int day ;
    int year ;
    int Month ;
    int Hour ;
    int minut;
    String KeyVisit;
    String PatientId;
    String VolunteerId;
    double Latitude;
    double Longitude;
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }



    public String getKeyVisit() {
        return KeyVisit;
    }

    public void setKeyVisit(String keyVisit) {
        KeyVisit = keyVisit;
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

    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
    }

    public String getVolunteerId() {
        return VolunteerId;
    }

    public void setVolunteerId(String volunteerId) {
        VolunteerId = volunteerId;
    }


    public int getHour() {
        return Hour;
    }

    public void setHour(int hour) {
        Hour = hour;
    }

    public int getMinut() {
        return minut;
    }

    public void setMinut(int minut) {
        this.minut = minut;
    }

    public visit() {
    }



    public visit(String statues, Date date, Time time, String place, String description) {
        Statues = statues;
        Place = place;
        Description = description;
    }

    public String getStatues() {
        return Statues;
    }

    public void setStatues(String statues) {
        Statues = statues;
    }





    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


}
