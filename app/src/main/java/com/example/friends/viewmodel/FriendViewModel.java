package com.example.friends.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.friends.entity.Friend;
import com.example.friends.model.FriendRepository;

import java.util.List;

public class FriendViewModel extends AndroidViewModel {

    private FriendRepository repository;
    private LiveData<List<Friend>> allFriends;

    public FriendViewModel(@NonNull Application application)
    {
        super(application);
        repository = new FriendRepository(application);
        allFriends = repository.getAll();
    }

    public void insert(Friend friend)
    {
        repository.insert(friend);
    }

    public void update(Friend friend)
    {
        repository.update(friend);
    }

    public void delete(Friend friend)
    {
        repository.delete(friend);
    }

    public LiveData<List<Friend>> getAll()
    {
        return allFriends;
    }
}
