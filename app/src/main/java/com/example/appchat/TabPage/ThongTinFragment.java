package com.example.appchat.TabPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.appchat.Model.User;
import com.example.appchat.R;
import com.example.appchat.StartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThongTinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThongTinFragment extends Fragment {
    private ImageView _ivbg;
    private CircleImageView _ivavatar;
    private TextView _tvDangXuat, _tvName;
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

        LoadProfile();
        FC_DangXuat();

       return root;
    }

    private void LoadProfile() {
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                Log.i("ThongTin USer", u.toString());
                assert u != null;
                if(!u.getImageURL().equals("default")){
                    Glide.with(getContext()).load(u.getImageURL()).into(_ivavatar);
                }
                _tvName.setText(u.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        _ivavatar = root.findViewById(R.id.ivavatar);
        _ivbg = root.findViewById(R.id.ivbg);
        _tvDangXuat = root.findViewById(R.id.tvDangXuat);
        _tvName = root.findViewById(R.id.tvName);
    }
}
