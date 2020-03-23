package com.example.friends.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Friend {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String phone;
    private String email;
    private boolean favorite;

    public Friend(String name, String phone, String email, boolean favorite) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.favorite = favorite;
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

    public String getEmail() {
        return email;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
