package com.example.appchat.TabPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appchat.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TinNhanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TinNhanFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tin_nhan, container, false);


        return root;
    }
}