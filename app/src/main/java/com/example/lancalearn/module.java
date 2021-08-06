package com.example.lancalearn;

public class module {
    private String name,code,course,borrowed;

    public module() {
    }

    public module(String name, String code, String course, String borrowed) {
        this.name = name;
        this.code = code;
        this.course = course;
        this.borrowed = borrowed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(String borrowed) {
        this.borrowed = borrowed;
    }
}
