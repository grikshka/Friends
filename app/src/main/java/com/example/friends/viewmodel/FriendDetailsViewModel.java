package com.example.friends.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.friends.entity.Friend;
import com.example.friends.model.FriendRepository;

public class FriendDetailsViewModel extends ViewModel {

    private FriendRepository repository;
    private LiveData<Friend> friend;

    public void initialize(Application application, int friendId)
    {
        if(repository == null)
        {
            repository = new FriendRepository(application);
            friend = repository.get(friendId);
        }
    }

    public LiveData<Friend> getFriend()
    {
        return friend;
    }
}
