package com.example.friends.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.friends.entity.Friend;

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
