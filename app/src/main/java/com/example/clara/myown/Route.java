package com.example.clara.myown;

/*
 * File: Route.java
 * Last edited: 28-3-2017
 * By: Clara Tump
 *
 * This is the model class for a climbing-route in the app. */

public class Route {
    private String name;
    private String grade;
    private String description;

    // Default constructor for Firebase
    public Route() {}

    public Route(String name, String grade, String description) {
        this.name = name;
        this.grade = grade;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}