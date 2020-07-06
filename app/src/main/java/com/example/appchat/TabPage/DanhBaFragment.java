package com.example.appchat.TabPage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Adapter.UserAdapter;
import com.example.appchat.Model.User;
import com.example.appchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class DanhBaFragment extends Fragment {

    private RecyclerView _rvDanhBa;
    private UserAdapter _userAdapter;
    private List<User> _mUsers;
    private FirebaseUser firebaseUser;

    private ProgressDialog progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_danh_ba, container, false);

        _rvDanhBa = root.findViewById(R.id.rvDanhBa);
        _rvDanhBa.setHasFixedSize(true);
        _rvDanhBa.setLayoutManager(new LinearLayoutManager(getContext()));
        _mUsers = new ArrayList<>();
        readUsers();
        FC_ShowProgrcess();
        return root;
    }

    private void FC_ShowProgrcess(){
        progressBar = new ProgressDialog(getContext(), R.style.MyTheme);
        progressBar.setCancelable(false);
        progressBar.show();
    }

    private void readUsers() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _mUsers.clear();
                for( DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    Log.i("Users ", user.getId() + "---" + firebaseUser.getUid());
                    if(!firebaseUser.getUid().equals(user.getId()) ){
                        _mUsers.add(user);
                    }
                }
                _userAdapter = new UserAdapter(getContext(), _mUsers, false);
                _rvDanhBa.setAdapter(_userAdapter);
                progressBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
