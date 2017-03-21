package com.example.clara.myown;

/**
 * Created by clara on 17-3-2017.
 */

public class TopEvent {
    public int time;
    public Route route;
    public User user;


    public TopEvent() {}

    public TopEvent(int time, Route route, User user) {
        this.time = time;
        this.route = route;
        this.user = user;
    }
}
