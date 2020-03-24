package com.example.friends.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.friends.R;
import com.example.friends.entity.Friend;
import com.example.friends.viewmodel.FriendsListViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private RecyclerView recFriends;
    private MaterialButton btnAddFriend;

    private FriendsListViewModel friendsListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        initializeViews();
        initializeViewModel();
        initializeRecyclerView();
        initializeAddFriendButton();
    }

    private void initializeViews()
    {
        recFriends = findViewById(R.id.recFriends);
        btnAddFriend = findViewById(R.id.btnAddFriend);
    }

    private void initializeViewModel()
    {
        friendsListViewModel = new ViewModelProvider(this).get(FriendsListViewModel.class);
        friendsListViewModel.initialize(getApplication());
    }

    private void initializeRecyclerView()
    {
        final FriendAdapter adapter = new FriendAdapter();
        recFriends.setLayoutManager(new LinearLayoutManager(this));
        recFriends.setHasFixedSize(true);
        recFriends.setAdapter(adapter);

        friendsListViewModel.getAllFriends().observe(this, new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friends) {
                adapter.setFriendList(friends);
            }
        });
    }

    private void initializeAddFriendButton()
    {
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddFriendActivity();
            }
        });

    }

    private void startAddFriendActivity()
    {
        Intent intent = new Intent(this, AddFriendActivity.class);
        startActivity(intent);
    }

}
