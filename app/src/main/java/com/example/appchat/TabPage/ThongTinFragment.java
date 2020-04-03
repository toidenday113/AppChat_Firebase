package com.example.appchat.TabPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.appchat.R;
import com.example.appchat.StartActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThongTinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThongTinFragment extends Fragment {
    private ImageView _ivbg;
    private TextView _tvDangXuat;
    private Animation _bganim;
    private View root;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_thong_tin, container, false);

        mAuth= FirebaseAuth.getInstance();
        AnhXa();

        _bganim = AnimationUtils.loadAnimation(getContext(), R.anim.bganim);
        _ivbg.animate().translationY(-700).setDuration(800).setStartDelay(300);
        FC_DangXuat();
       return root;
    }

    private void FC_DangXuat(){
        _tvDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intentStart = new Intent(getActivity(), StartActivity.class);
                startActivity(intentStart);
                getActivity().finish();
            }
        });
    }

    private void AnhXa(){
        _ivbg = root.findViewById(R.id.ivbg);
        _tvDangXuat = root.findViewById(R.id.tvDangXuat);
    }
}
