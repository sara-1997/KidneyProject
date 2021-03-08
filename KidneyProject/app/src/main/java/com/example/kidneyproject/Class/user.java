package com.example.kidneyproject.Class;

public class user {
     private String name;
    private String password;
    private String emailAddress;
    private String phone;
    private String typeuser ;
    private String Token , imageUrl;
    private String Place ;
    double Latitude;
    double Longitude;


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


    public user() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTypeuser(String typeuser) {
        this.typeuser = typeuser;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public user(String name, String password, String emailAddress, String phone, String typeuser, String token, String place , String image) {
        this.name = name;
        this.password = password;
        this.emailAddress = emailAddress;
        this.phone = phone;
        this.typeuser = typeuser;
        Token = token;
        Place = place;
        imageUrl=image;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhone() {
        return phone;
    }

    public String getTypeuser() {
        return typeuser;
    }
}