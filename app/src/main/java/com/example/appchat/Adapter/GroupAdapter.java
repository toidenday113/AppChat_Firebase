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
import com.example.appchat.MessagerGroupActivity;
import com.example.appchat.Model.GroupChat;
import com.example.appchat.Model.mGroup;
import com.example.appchat.R;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private Context context;
    private List<GroupChat> arrGroupChat;
    private List<mGroup> arrGroup;
    public GroupAdapter() {
    }

    public GroupAdapter(Context context, List<GroupChat> arrGroupChat, List<mGroup> arrMGroup) {
        this.context = context;
        this.arrGroupChat = arrGroupChat;
        this.arrGroup = arrMGroup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View viewroot = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);

        return new GroupAdapter.ViewHolder(viewroot);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            GroupChat groupChat = arrGroupChat.get(position);
            holder.tvName.setText(groupChat.getName());

            if(!groupChat.getAvatar().equals("default")){
                Glide.with(context).load(groupChat.getAvatar()).into(holder.ivAvatar);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    GroupChat item = arrGroupChat.get(position);

                    Intent intentMessagerGroup = new Intent(context, MessagerGroupActivity.class);
                    intentMessagerGroup.putExtra("itemGroup",  item);
                    context.startActivity(intentMessagerGroup);
                }
            });
    }

    @Override
    public int getItemCount() {
        return arrGroupChat.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatar;
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.profile_image);
            tvName = itemView.findViewById(R.id.tvUserName);

        }
    }
}
