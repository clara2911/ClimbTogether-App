package com.example.clara.myown;

import java.util.ArrayList;

/*
 * File: User.java
 * Last edited: 28-3-2017
 * By: Clara Tump
 *
 * This is the model class for a user of the app. */

public class User {
    private String email;
    private String name;
    private ArrayList<Route> savedRoutes;

    // Default constructor for Firebase
    public User() {}

    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.savedRoutes = new ArrayList<>();
    }

    public User(String email, String name, ArrayList<Route> routes) {
        this.email = email;
        this.name = name;
        this.savedRoutes = routes;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Route> getSavedRoutes() {
        return savedRoutes;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSavedRoutes(ArrayList<Route> savedRoutes) {
        this.savedRoutes = savedRoutes;
    }
}