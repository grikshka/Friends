package com.example.friends.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                    addCallback(populateCallback).
                    build();
        }
        return instance;
    }

    private static RoomDatabase.Callback populateCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

        }
    };

    private static class PopulateTask extends AsyncTask<Void, Void, Void> {

        private FriendDao friendDao;

        private PopulateTask(FriendsDatabase db)
        {
            friendDao = db.friendDao();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            friendDao.insert(new Friend("Grzegorz Charyszczak", "schemabuoi@gmail.com", true));
            friendDao.insert(new Friend("Oliwia Skrzypaszek", "oliwiaskrzypaszek@gmail.com", true));
            friendDao.insert(new Friend("Marcin Dwornikowski", "mdwornikowski@gmail.com", true));
            friendDao.insert(new Friend("Iga Molendowska", "igamolendowska@gmail.com", true));
            friendDao.insert(new Friend("David Kalatzis", "davidkalatzis@gmail.com", true));
            friendDao.insert(new Friend("Mate Kiss", "matekiss@gmail.com", false));
            friendDao.insert(new Friend("Nedas Surkus", "nedassurkus@gmail.com", false));
            friendDao.insert(new Friend("Marek Stancik", "marekstancik@gmail.com", false));
            friendDao.insert(new Friend("Jan Toth", "jantoth@gmail.com", false));
            friendDao.insert(new Friend("Alex Pedersen", "alexpedersen@gmail.com", false));
            friendDao.insert(new Friend("Kuba Rewald", "kubarewald@gmail.com", false));
            friendDao.insert(new Friend("Radoslaw Haller", "radoslawhaller@gmail.com", false));
            friendDao.insert(new Friend("Adi Tutor", "aditutor@gmail.com", false));
            return null;
        }
    }
}
