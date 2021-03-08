package com.example.kidneyproject.Class;

public class talent {
    String PatientId , VolunteerId , ImagePatient , Title , ImageTalent , Key ,Likemy;

    public talent(String patientId, String volunteerId, String imagePatient, String title, String imageTalent, String key, String likemy, int numberlike) {
        PatientId = patientId;
        VolunteerId = volunteerId;
        ImagePatient = imagePatient;
        Title = title;
        ImageTalent = imageTalent;
        Key = key;
        Likemy = likemy;
        this.numberlike = numberlike;
    }

    int  numberlike;

    public String getLikemy() {
        return Likemy;
    }

    public void setLikemy(String likemy) {
        Likemy = likemy;
    }

    public talent(String patientId, String volunteerId, String imagePatient, String title, String imageTalent, String key,
                  int numberlike) {
        PatientId = patientId;
        VolunteerId = volunteerId;
        ImagePatient = imagePatient;
        Title = title;
        ImageTalent = imageTalent;
        Key = key;
        this.numberlike = numberlike;
    }

    public int getNumberlike() {
        return numberlike;
    }

    public void setNumberlike(int numberlike) {
        this.numberlike = numberlike;
    }

    public talent() {
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

    public String getImagePatient() {
        return ImagePatient;
    }

    public void setImagePatient(String imagePatient) {
        ImagePatient = imagePatient;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImageTalent() {
        return ImageTalent;
    }

    public void setImageTalent(String imageTalent) {
        ImageTalent = imageTalent;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
