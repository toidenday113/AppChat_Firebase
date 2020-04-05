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

public class MessagerAdapter extends RecyclerView.Adapter<MessagerAdapter.ViewHolder> {

    private Context _mContext;
    private List<User> _mUser;

    public MessagerAdapter (Context mContext, List<User> mUser){
        this._mContext = mContext;
        this._mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_mContext).inflate(R.layout.user_item, parent, false);
        return new MessagerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = _mUser.get(position);
        holder.username.setText(user.getUsername());

        if(user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(_mContext).load(user.getImageURL()).into(holder.profile_image);
        }
    }

    @Override
    public int getItemCount() {
        return _mUser.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        ImageView profile_image;

        ViewHolder(View itemView){
            super(itemView);

            //username = itemView.findViewById(R.id.username);
        }
    }
}
