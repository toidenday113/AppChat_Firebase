package com.example.appchat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Model.MessegerGroupChat;
import com.example.appchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessagerGroupAdapter extends RecyclerView.Adapter<MessagerGroupAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT= 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context _mContext;
    private List<MessegerGroupChat> arrMessegeGroup;

    public MessagerGroupAdapter(Context _mContext, List<MessegerGroupChat> arrMessegeGroup) {
        this._mContext = _mContext;
        this.arrMessegeGroup = arrMessegeGroup;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MessegerGroupChat mgc = arrMessegeGroup.get(position);

            holder.tvContent.setText(mgc.getContent());
    }

    @Override
    public int getItemCount() {
        return arrMessegeGroup.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_Message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        assert fUser != null;
        if(arrMessegeGroup.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }

    }
}
