package com.example.friends.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.friends.R;
import com.example.friends.entity.Friend;
import com.example.friends.viewmodel.FriendsListViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private RecyclerView recFriends;

    private FriendsListViewModel friendsListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        initializeViews();
        initializeViewModel();
        setUpActionBar();
        initializeRecyclerView();
    }

    private void initializeViews()
    {
        recFriends = findViewById(R.id.recFriends);
    }

    /*
        In the future this code should be replaced by using
        dagger dependency injection tool
     */
    private void initializeViewModel()
    {
        friendsListViewModel = new ViewModelProvider(this).get(FriendsListViewModel.class);
        friendsListViewModel.initialize(getApplication());
    }

    /*
        Method for setting action bar to our own customized toolbar
     */
    private void setUpActionBar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /*
       Method invoked when options menu gets created.
       We want to set the menu for this activity here.
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friends_list, menu);
        return true;
    }

    /*
        Method invoked after user performed some action by clicking
        something from the action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_add:
            {
                startAddFriendActivity();
                return true;
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
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

    private void startAddFriendActivity()
    {
        Intent intent = new Intent(this, AddFriendActivity.class);
        startActivity(intent);
    }

}
