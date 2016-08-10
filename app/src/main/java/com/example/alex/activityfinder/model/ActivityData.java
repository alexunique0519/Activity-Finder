package com.example.alex.activityfinder.model;

import android.graphics.Bitmap;

import java.util.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 2016-07-25.
 */
public class ActivityData {

    private String activityName;
    private String activityOwner;
    private String description;
    private Bitmap bmp;
    private String imageString;
    private float longitude;
    private float latitude;
    private String location_line1;
    private String location_line2;
    private Date date;
    private ArrayList<JoinMessage> joinMessagesList;

    public ActivityData(String activityName, String activityOwner, String description, Bitmap bmp, float longitude, float latitude, String location1, String location2, Date date) {
        this.activityName = activityName;
        this.activityOwner = activityOwner;
        this.description = description;
        this.bmp = bmp;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location_line1 = location1;
        this.location_line2 = location2;
        this.date = date;
        joinMessagesList = new ArrayList<JoinMessage>();
        this.imageString = "";
    }

    public ActivityData() {
        this.activityName = null;
        this.activityOwner = null;
        this.description = null;
        this.bmp = null;
        this.longitude = 0;
        this.latitude = 0;
        this.location_line1 = null;
        this.location_line2 = null;
        this.date = null;
        joinMessagesList = new ArrayList<JoinMessage>();
        this.imageString = null;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityOwner() {
        return activityOwner;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
        return bmp;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getLocation_line1() {
        return location_line1;
    }

    public String getLocation_line2() {return location_line2;}

    public Date getDate() {
        return date;
    }

    public void setActivityOwner(String activityOwner) {
        this.activityOwner = activityOwner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Bitmap image) {
        this.bmp = image;
    }

    public void setImageString(String imageString){
        this.imageString = imageString;
    }

    public String getImageString(){
        return  this.imageString;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLocation_line1(String location) {
        this.location_line1 = location;
    }

    public void setLocation_line2(String location) { this.location_line2 = location; }

    public void setDate(Date date) {
        this.date = date;
    }


}


