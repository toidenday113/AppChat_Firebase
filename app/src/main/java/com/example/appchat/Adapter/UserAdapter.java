package com.example.appchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat.MessagerActivity;
import com.example.appchat.Model.User;
import com.example.appchat.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context _mContext;
    private List<User> _mUser;

    public UserAdapter (Context mContext, List<User> mUser){
        this._mContext = mContext;
        this._mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           final User user = _mUser.get(position);
            holder.username.setText(user.getUsername());

            if(user.getImageURL().equals("default")){
                holder.profile_image.setImageResource(R.mipmap.ic_launcher);
            }else{
                Glide.with(_mContext).load(user.getImageURL()).into(holder.profile_image);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentMessager = new Intent(_mContext, MessagerActivity.class);
                    intentMessager.putExtra("userid", user.getId());
                    _mContext.startActivity(intentMessager);
                    //Toast.makeText(_mContext,  user.toString(), Toast.LENGTH_SHORT).show();
                }
            });
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

            username = itemView.findViewById(R.id.tvUserName);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }
}
