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
import com.example.appchat.Model.Chat;
import com.example.appchat.Model.User;
import com.example.appchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context _mContext;
    private List<User> _mUser;
    private boolean _isChat;

    private String theLastMsg;

    public UserAdapter (Context mContext, List<User> mUser, boolean isChat){
        this._mContext = mContext;
        this._mUser = mUser;
        this._isChat = isChat;
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

            if (user.getStatus().equals("online")) {
                holder.img_Online.setVisibility(View.VISIBLE);
                holder.img_Offline.setVisibility(View.GONE);
            } else {
                holder.img_Online.setVisibility(View.GONE);
                holder.img_Offline.setVisibility(View.VISIBLE);
            }

            if(_isChat){
                lastMessage(user.getId(), holder.last_msg);
            }else{
                holder.last_msg.setVisibility(View.GONE);
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
        ImageView img_Online;
        ImageView img_Offline;
        TextView last_msg;


        ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.tvUserName);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_Online = itemView.findViewById(R.id.img_online);
            img_Offline = itemView.findViewById(R.id.img_offline);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }

    private void lastMessage(final String userid, final TextView last_msg){
        theLastMsg = "default";
        final FirebaseUser  firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                        chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(userid)){
                        theLastMsg = chat.getMessage();
                    }
                }

                switch(theLastMsg){
                    case"default":
                            last_msg.setText("No Message");
                            break;
                    default:
                        last_msg.setText(theLastMsg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
