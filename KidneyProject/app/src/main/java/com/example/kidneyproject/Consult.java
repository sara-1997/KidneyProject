package com.example.kidneyproject;

class Consult {
    String PatientId , Title , descriptipn , Key;

    public Consult() {
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
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
