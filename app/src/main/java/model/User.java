package model;

import android.media.Image;

/**
 * Created by Alex on 2016-06-27.
 */
public class User {
    private int id;
    private String imagePath;
    private String name;

    public User() {
    }

    public User(int id, String imagePath, String name) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
