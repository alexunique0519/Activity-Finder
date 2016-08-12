package com.example.alex.activityfinder.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alex on 2016-08-02.
 */
public class JoinMessage implements Parcelable{

    public JoinMessage(String messageSender, String message) {
        this.messageSender = messageSender;
        this.message = message;
    }

    private  String messageSender;
    private  String message;


    protected JoinMessage(Parcel in) {
        messageSender = in.readString();
        message = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(messageSender);
        dest.writeString(message);
    }

    public static final Creator<JoinMessage> CREATOR = new Creator<JoinMessage>() {
        @Override
        public JoinMessage createFromParcel(Parcel in) {
            return new JoinMessage(in);
        }

        @Override
        public JoinMessage[] newArray(int size) {
            return new JoinMessage[size];
        }
    };
}