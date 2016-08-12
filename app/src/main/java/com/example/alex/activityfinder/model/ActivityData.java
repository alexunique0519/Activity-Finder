package com.example.alex.activityfinder.model;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 2016-07-25.
 */
public class ActivityData implements Parcelable{

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
    private String uniqueKey;
    private ArrayList<JoinMessage> joinMessagesList;

    public ActivityData(String key, String activityName, String activityOwner, String description, Bitmap bmp, float longitude, float latitude, String location1, String location2, Date date) {
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
        this.uniqueKey = key;
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
        this.uniqueKey = null;
    }

    protected ActivityData(Parcel in) {
        activityName = in.readString();
        activityOwner = in.readString();
        description = in.readString();
        bmp = in.readParcelable(Bitmap.class.getClassLoader());
        imageString = in.readString();
        longitude = in.readFloat();
        latitude = in.readFloat();
        location_line1 = in.readString();
        location_line2 = in.readString();
        uniqueKey = in.readString();
        in.readTypedList(joinMessagesList, JoinMessage.CREATOR);
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

    public String getUniqueKey(){return this.uniqueKey;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       /*
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
    private String uniqueKey;
    private ArrayList<JoinMessage> joinMessagesList;
       * */
        dest.writeString(activityName);
        dest.writeString(activityOwner);
        dest.writeString(description);
        dest.writeValue(bmp);
        dest.writeString(imageString);
        dest.writeFloat(longitude);
        dest.writeFloat(latitude);
        dest.writeString(location_line1);
        dest.writeString(location_line2);
        dest.writeLong(date.getTime());
        dest.writeString(uniqueKey);

       dest.writeTypedList(joinMessagesList);
    }

    public static final Creator<ActivityData> CREATOR = new Creator<ActivityData>() {
        @Override
        public ActivityData createFromParcel(Parcel in) {
            return new ActivityData(in);
        }

        @Override
        public ActivityData[] newArray(int size) {
            return new ActivityData[size];
        }
    };
}


