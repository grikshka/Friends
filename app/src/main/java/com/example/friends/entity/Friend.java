package com.example.friends.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Friend implements Serializable {

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

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
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
