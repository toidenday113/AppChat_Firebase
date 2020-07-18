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
import com.example.appchat.Model.MessegerGroupChat;
import com.example.appchat.Model.User;
import com.example.appchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessagerGroupAdapter extends RecyclerView.Adapter<MessagerGroupAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT= 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context _mContext;
    private List<MessegerGroupChat> arrMessegeGroup;
    private FirebaseUser fUser;
    private String idGroup;

    public MessagerGroupAdapter(Context _mContext, List<MessegerGroupChat> arrMessegeGroup, String idGroup) {
        this._mContext = _mContext;
        this.arrMessegeGroup = arrMessegeGroup;
        this.idGroup = idGroup;
        fUser = FirebaseAuth.getInstance().getCurrentUser();

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(_mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessagerGroupAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(_mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessagerGroupAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            final MessegerGroupChat mgc = arrMessegeGroup.get(position);

            if(!fUser.getUid().equals(mgc.getSender())) {
                DatabaseReference RefUser = FirebaseDatabase.getInstance().getReference("Users").child(mgc.getSender());
                RefUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User u = snapshot.getValue(User.class);
                        if(! u.getImageURL().equals("default")){
                            Glide.with(_mContext).load(u.getImageURL()).into(holder.ivAvatar);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            if(! mgc.getImage().equals("")){
                holder.tvContent.setVisibility(View.GONE);
                holder.ivImageMessenger.setVisibility(View.VISIBLE);
                Glide.with(_mContext).load(mgc.getImage()).into(holder.ivImageMessenger);
            }else{

                holder.ivImageMessenger.setVisibility(View.GONE);
                holder.tvContent.setVisibility(View.VISIBLE);
                holder.tvContent.setText(mgc.getContent());
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
                            DatabaseReference RefDelete = FirebaseDatabase.getInstance().getReference("Groups").child(idGroup).child("Messengers").child(arrMessegeGroup.get(position).getId());
                            RefDelete.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
//                                        arrMessegeGroup.remove(position);
                                        notifyDataSetChanged();

                                        bottomSheetDialog.cancel();
                                    }
                                }
                            });


                        }
                    });

                    return true;
                }
            });


    }

    @Override
    public int getItemCount() {
        return arrMessegeGroup.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvContent;
        ImageView ivAvatar, ivImageMessenger;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_Message);
            ivAvatar = itemView.findViewById(R.id.profile_image);
            ivImageMessenger = itemView.findViewById(R.id.image_messenger);
        }
    }

    @Override
    public int getItemViewType(int position) {
        assert fUser != null;
        if(arrMessegeGroup.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }

    }
}
