package com.example.appchat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat.Model.User;
import com.example.appchat.R;

import java.util.List;

public class UserAddGroupAdapter  extends RecyclerView.Adapter<UserAddGroupAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mUser;
    private List<String> arrUserJoinGroup;

    public UserAddGroupAdapter(Context mContext, List<User> mUser) {
        this.mContext = mContext;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_add_group, parent, false);

        return new UserAddGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            User u = mUser.get(position);

            holder.tvUserName.setText(u.getUsername());
            if(u.getImageURL().equals("default")){
                Glide.with(mContext).load(u.getImageURL()).into(holder.ivAvatar);
            }
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{

        public ImageView ivAvatar;
        public TextView tvUserName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.CircleImageView_Avatar_Group);
            tvUserName = itemView.findViewById(R.id.TextView_Name_User);
        }
    }
}
