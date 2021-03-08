package com.example.kidneyproject;

class Suggistions {
    String Userid , Title , descriptipn , Key;

    public Suggistions() {
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getPatientId() {
        return Userid;
    }

    public void setPatientId(String patientId) {
        Userid = patientId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescriptipn() {
        return descriptipn;
    }

    public void setDescriptipn(String descriptipn) {
        this.descriptipn = descriptipn;
    }
}
