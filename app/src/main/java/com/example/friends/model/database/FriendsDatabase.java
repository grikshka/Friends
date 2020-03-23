package com.example.friends.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.friends.entity.Friend;

@Database(entities = {Friend.class}, version = FriendsDatabase.VERSION)
public abstract class FriendsDatabase extends RoomDatabase {

    public static final int VERSION = 1;

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
            new PopulateTask(instance).execute();
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
            friendDao.insert(new Friend("Grzegorz Charyszczak", "+48 502 288 995", "schemabuoi@gmail.com", true));
            friendDao.insert(new Friend("Oliwia Skrzypaszek", "+48 690 421 069", "oliwiaskrzypaszek@gmail.com", true));
            friendDao.insert(new Friend("Marcin Dwornikowski", "+48 607 412 984", "mdwornikowski@gmail.com", true));
            friendDao.insert(new Friend("Iga Molendowska", "+48 883 736 886", "igamolendowska@gmail.com", true));
            friendDao.insert(new Friend("David Kalatzis", "+45 81 90 84 96", "davidkalatzis@gmail.com", true));
            friendDao.insert(new Friend("Mate Kiss", "+45 50 32 43 85", "matekiss@gmail.com", false));
            friendDao.insert(new Friend("Nedas Surkus", "+45 16 78 34 72", "nedassurkus@gmail.com", false));
            friendDao.insert(new Friend("Marek Stancik", "+45 74 67 83 12", "marekstancik@gmail.com", false));
            friendDao.insert(new Friend("Jan Toth", "+45 66 40 15 91", "jantoth@gmail.com", false));
            friendDao.insert(new Friend("Alex Pedersen", "+45 40 31 15 20", "alexpedersen@gmail.com", false));
            friendDao.insert(new Friend("Kuba Rewald", "+45 72 21 14 15", "kubarewald@gmail.com", false));
            friendDao.insert(new Friend("Radoslaw Haller", "+45 40 20 76 91", "radoslawhaller@gmail.com", false));
            friendDao.insert(new Friend("Adi Tutor", "+45 59 91 15 20", "aditutor@gmail.com", false));
            return null;
        }
    }
}
