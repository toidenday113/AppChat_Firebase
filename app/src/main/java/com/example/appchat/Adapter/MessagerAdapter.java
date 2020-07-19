package com.example.appchat.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat.Model.Chat;
import com.example.appchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagerAdapter extends RecyclerView.Adapter<MessagerAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT= 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context _mContext;
    private List<Chat> _mChat;
    private String _Images_url;
    private FirebaseUser _fuser;
    public MessagerAdapter (Context mContext, List<Chat> mChat, String imageURL){
        this._mContext = mContext;
        this._mChat = mChat;
        this._Images_url = imageURL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(_mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessagerAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(_mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessagerAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Chat chat = _mChat.get(position);

        if(!_Images_url.equals("default")){
            Log.i("_Images_url", _Images_url);
            Glide.with(_mContext).load(_Images_url).into(holder.Image_Avatar);
        }
        if(!chat.getImage().equals("")){
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.Message.setVisibility(View.GONE);
            Glide.with(_mContext).load(chat.getImage()).into(holder.ivImage);
        }else{
            holder.ivImage.setVisibility(View.GONE);
            holder.Message.setVisibility(View.VISIBLE);
            holder.Message.setText(chat.getMessage());
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(_mContext);
                View view = LayoutInflater.from(_mContext).inflate(R.layout.layput_bottom_sheet_delete_messenger, null);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();
                view.findViewById(R.id.textView_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteItemMessenger(position);
                        bottomSheetDialog.cancel();
                    }
                });
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return _mChat.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView Message;
        CircleImageView Image_Avatar;
        ImageView ivImage;

        ViewHolder(View itemView){
            super(itemView);

            Message = itemView.findViewById(R.id.tv_Message);
            Image_Avatar = itemView.findViewById(R.id.profile_image);
            ivImage = itemView.findViewById(R.id.image_messenger);
        }
    }

    @Override
    public int getItemViewType(int position) {
        _fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (!_mChat.get(position).getSender().equals(_fuser.getUid())) {
            return MSG_TYPE_LEFT;
        } else {
            return MSG_TYPE_RIGHT;
        }
    }

    private void DeleteItemMessenger(int position){
        DatabaseReference RefDelete = FirebaseDatabase.getInstance().getReference("Chats").child(_mChat.get(position).getId());
        RefDelete.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    notifyDataSetChanged();
                }
            }
        });
    }
}
