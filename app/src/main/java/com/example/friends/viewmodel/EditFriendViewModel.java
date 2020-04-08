package com.example.friends.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.example.friends.entity.Friend;
import com.example.friends.model.FriendRepository;

public class EditFriendViewModel extends ViewModel {

    private FriendRepository repository;

    public void initialize(Application application)
    {
        if(repository == null)
        {
            repository = new FriendRepository(application);
        }
    }

    public void updateFriend(Friend friend)
    {
        repository.update(friend);
    }
}
