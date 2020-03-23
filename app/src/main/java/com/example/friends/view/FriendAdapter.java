package com.example.friends.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friends.R;
import com.example.friends.entity.Friend;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder>
{

    private List<Friend> friendList;

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.friend_item, parent, false);
        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        if(friendList != null)
        {
            Friend currentFriend = friendList.get(position);
            holder.tvName.setText(currentFriend.getName());
            if (currentFriend.isFavorite())
            {
                holder.imgFavorite.setImageResource(R.drawable.ic_favorite_true);
            }
            else
            {
                holder.imgFavorite.setImageResource(R.drawable.ic_favorite_false);
            }
        }

    }

    @Override
    public int getItemCount() {
        if(friendList != null)
        {
            return friendList.size();
        }
        else
        {
            return 0;
        }
    }

    public void setFriendList(List<Friend> friendList)
    {
        this.friendList = friendList;
        notifyDataSetChanged();
    }

    class FriendHolder extends RecyclerView.ViewHolder
    {
        private CircleImageView imgProfilePicture;
        private TextView tvName;
        private ImageView imgFavorite;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);
            imgProfilePicture = itemView.findViewById(R.id.imgProfilePicture);
            tvName = itemView.findViewById(R.id.tvName);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
        }
    }
}
