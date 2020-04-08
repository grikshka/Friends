package com.example.friends.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.friends.R;
import com.example.friends.entity.Friend;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends ListAdapter<Friend, FriendAdapter.FriendHolder>
{
    private OnItemClickListener listener;

    public FriendAdapter() {
        super(DIFF_CALLBACK);
    }

    private static DiffUtil.ItemCallback<Friend> DIFF_CALLBACK = new DiffUtil.ItemCallback<Friend>() {
        @Override
        public boolean areItemsTheSame(@NonNull Friend oldItem, @NonNull Friend newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Friend oldItem, @NonNull Friend newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.friend_item, parent, false);
        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        holder.setView(getItem(position));
    }

    class FriendHolder extends RecyclerView.ViewHolder
    {
        private Context context;
        private Friend friend;

        private CircleImageView imgProfilePicture;
        private TextView tvName;
        private ImageView imgFavorite;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            initializeViews();
            initializeListener(itemView);
        }

        private void initializeViews()
        {
            imgProfilePicture = itemView.findViewById(R.id.imgProfilePicture);
            tvName = itemView.findViewById(R.id.tvName);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
        }

        private void initializeListener(View view)
        {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(friend);
                    }
                }
            });
        }

        /*
            Sets view to the data of passed friend object.
         */
        public void setView(Friend friend)
        {
            this.friend = friend;
            tvName.setText(friend.getName());

            if (friend.isFavorite())
            {
                imgFavorite.setImageResource(R.drawable.ic_favorite_true);
            }
            else
            {
               imgFavorite.setImageResource(R.drawable.ic_favorite_false);
            }

            if(!(friend.getPicturePath() == null) && !friend.getPicturePath().isEmpty())
            {
                setProfilePicture(friend.getPicturePath());
            }
            else
            {
                imgProfilePicture.setImageResource(R.drawable.user_icon);
            }
        }

        /*
            Using Glide library for setting the profile picture to benefit
            from image caching and simplifying complex task of setting bitmap
            to achieve smooth behaviour and fast decoding
        */
        private void setProfilePicture(String pathToImage)
        {
            Glide.with(context)
                    .load(pathToImage)
                    .into(imgProfilePicture);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Friend friend);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

}
