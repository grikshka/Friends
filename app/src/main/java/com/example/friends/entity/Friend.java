package com.example.friends.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Friend {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String phone;
    private Date birthday;
    private String email;
    private String website;
    private boolean favorite;
    private String picturePath;

    public Friend(String name, String phone, Date birthday, String email, String website, boolean favorite, String picturePath) {
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.email = email;
        this.website = website;
        this.favorite = favorite;
        this.picturePath = picturePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone()
    {
        return phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getPicturePath() {
        return picturePath;
    }
}
