package com.example.appchat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat.Model.User;
import com.example.appchat.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAddGroupAdapter  extends RecyclerView.Adapter<UserAddGroupAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mUser;
    private List<String> arrUserJoinGroup;

    public UserAddGroupAdapter(Context mContext, List<User> mUser) {
        this.mContext = mContext;
        this.mUser = mUser;
        this.arrUserJoinGroup = new ArrayList<>();
    }

    public List<String> getArrUserJoinGroup() {
        return arrUserJoinGroup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_add_group, parent, false);

        return new UserAddGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            User u = mUser.get(position);

            holder.tvUserName.setText(u.getUsername());
            if(! u.getImageURL().equals("default")){
               Glide.with(mContext).load(u.getImageURL()).into(holder.ivAvatar);
            }

            holder.cbCheckUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.cbCheckUser.isChecked()){
                       // Toast.makeText(mContext, mUser.get(position).getUsername(), Toast.LENGTH_SHORT).show();
                        arrUserJoinGroup.add(mUser.get(position).getId());
                        Toast.makeText(mContext, arrUserJoinGroup.size()+"", Toast.LENGTH_SHORT).show();
                    }else{
                        if(arrUserJoinGroup.contains( mUser.get(position).getId()) ){
                            int p = arrUserJoinGroup.indexOf(mUser.get(position).getId());
                            //Toast.makeText(mContext, p+"", Toast.LENGTH_SHORT).show();
                            arrUserJoinGroup.remove(p);
                        }
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{

        public CircleImageView ivAvatar;
        public TextView tvUserName;
        public CheckBox cbCheckUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.CircleImageView_avatar_user);
            tvUserName = itemView.findViewById(R.id.TextView_Name_User);
            cbCheckUser = itemView.findViewById(R.id.checkbox_add_user_group);
        }
    }
}
