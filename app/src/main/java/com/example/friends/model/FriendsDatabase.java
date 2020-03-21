package com.example.friends.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Friend.class}, version = 1)
public abstract class FriendsDatabase extends RoomDatabase {

    private static FriendsDatabase instance;

    public abstract FriendDao friendDao();

    public static synchronized FriendsDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FriendsDatabase.class, "friends_database").
                    fallbackToDestructiveMigration().
                    build();
        }
        return instance;
    }
}
