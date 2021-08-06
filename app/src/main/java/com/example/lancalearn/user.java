package com.example.lancalearn;

public class user {
    private String name,phone,email,profile;

    public user() {
    }

    public user(String name, String phone, String email, String profile) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.profile = profile;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
