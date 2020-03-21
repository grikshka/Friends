package com.example.friends.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.friends.entity.Friend;
import com.example.friends.model.database.FriendDao;
import com.example.friends.model.database.FriendsDatabase;

import java.util.List;

public class FriendRepository {

    private FriendDao friendDao;
    private LiveData<List<Friend>> allFriends;

    public FriendRepository(Application application)
    {
        FriendsDatabase database = FriendsDatabase.getInstance(application);
        friendDao = database.friendDao();
        allFriends = friendDao.getAll();
    }

    public void insert(Friend friend)
    {
        new InsertTask(friendDao).execute(friend);
    }

    public void update(Friend friend)
    {
        new UpdateTask(friendDao).execute(friend);
    }

    public void delete(Friend friend)
    {
        new DeleteTask(friendDao).execute(friend);
    }

    public LiveData<List<Friend>> getAll()
    {
        return allFriends;
    }

    private static class InsertTask extends AsyncTask<Friend, Void, Void>
    {
        private FriendDao friendDao;

        private InsertTask(FriendDao friendDao)
        {
            this.friendDao = friendDao;
        }

        @Override
        protected Void doInBackground(Friend... friends)
        {
            friendDao.insert(friends[0]);
            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<Friend, Void, Void>
    {
        private FriendDao friendDao;

        private UpdateTask(FriendDao friendDao)
        {
            this.friendDao = friendDao;
        }

        @Override
        protected Void doInBackground(Friend... friends)
        {
            friendDao.update(friends[0]);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Friend, Void, Void>
    {
        private FriendDao friendDao;

        private DeleteTask(FriendDao friendDao)
        {
            this.friendDao = friendDao;
        }

        @Override
        protected Void doInBackground(Friend... friends)
        {
            friendDao.delete(friends[0]);
            return null;
        }
    }
}
