package com.example.appchat.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Model.MessegerGroupChat;

import java.util.List;

public class MessagerGroupAdapter extends RecyclerView.Adapter<MessagerGroupAdapter.ViewHolder> {

    private Context _mContext;

    public MessagerGroupAdapter(Context _mContext, List<MessegerGroupChat> arrMessegeGroup) {
        this._mContext = _mContext;
        this.arrMessegeGroup = arrMessegeGroup;
    }

    private List<MessegerGroupChat> arrMessegeGroup;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrMessegeGroup.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
