package com.example.clara.myown;

import java.util.ArrayList;

/**
 * Created by clara on 17-3-2017.
 */

public class User {
    public String email;
    public String name;
    public ArrayList<Route> toppedRoutes;

    public User() {}

    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.toppedRoutes = new ArrayList<Route>();
    }

    public User(String email, String name, ArrayList<Route> routes) {
        this.email = email;
        this.name = name;
        this.toppedRoutes = routes;
    }

    public void setToppedRoutes(ArrayList<Route> toppedRoutes) {
        this.toppedRoutes = toppedRoutes;
    }
}
