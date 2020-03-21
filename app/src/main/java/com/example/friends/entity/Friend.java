package com.example.friends.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Friend {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String email;
    private boolean favourite;

    public Friend(String name, String email, boolean favourite) {
        this.name = name;
        this.email = email;
        this.favourite = favourite;
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

    public String getEmail() {
        return email;
    }

    public boolean isFavourite() {
        return favourite;
    }
}
