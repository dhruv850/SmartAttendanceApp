package com.example.jaky_d.smartattendance;
public class UserInformation {

    private long enrollment;
    private String email;
    private String password;
    //Maybe you want to use double, change it to double
    private float latitude;
    private float longitude;

    public UserInformation() {
    }

    public UserInformation(long enrollment, String email, String password, float latitude, float longitude) {
        this.enrollment = enrollment;
        this.email = email;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(long enrollment) {
        this.enrollment = enrollment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}