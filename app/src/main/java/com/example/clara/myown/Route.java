package com.example.clara.myown;

import java.util.ArrayList;

/**
 * Created by clara on 17-3-2017.
 */

public class Route {
    public String name;
    public String grade;
    public String description;
    public ArrayList<User> toppedBy;

    // Default constructor for Firebase
    public Route() {}

    public Route(String name, String grade, String description) {
        this.name = name;
        this.grade = grade;
        this.description = description;
    }
}
