package com.example.alex.activityfinder.model;

/**
 * Created by Alex on 2016-08-02.
 */
public class JoinMessage{

    public JoinMessage(String messageSender, String message) {
        this.messageSender = messageSender;
        this.message = message;
    }

    private  String messageSender;
    private  String message;


}