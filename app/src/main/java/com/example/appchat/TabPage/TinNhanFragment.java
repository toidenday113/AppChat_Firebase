package com.example.appchat.TabPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Adapter.UserAdapter;
import com.example.appchat.Model.ChatList;
import com.example.appchat.Model.User;
import com.example.appchat.Notifications.Token;
import com.example.appchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;


public class TinNhanFragment extends Fragment {
    private UserAdapter userAdapter;
    private List<User> _mUsers;

    private FirebaseUser _fUser;
    private DatabaseReference reference;

    private List<ChatList> _userList;

    private RecyclerView _rv_TinNhan;
    private View root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tin_nhan, container, false);

        AnhXa();
        _rv_TinNhan.setHasFixedSize(true);
        _rv_TinNhan.setLayoutManager(new LinearLayoutManager(getContext()) );

        _fUser = FirebaseAuth.getInstance().getCurrentUser();

        _userList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(_fUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _userList.clear();

                for( DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ChatList chatlist = snapshot.getValue(ChatList.class);
                        _userList.add(chatlist);
                }
                ReadChatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());
        return root;
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(_fUser.getUid()).setValue(token1);
    }

    private void ReadChatList() {
        _mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _mUsers.clear();
                for( DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for (ChatList chatlist : _userList){
                        if(user.getId().equals(chatlist.getId())){
                            _mUsers.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), _mUsers, true);
                _rv_TinNhan.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void AnhXa() {
        _rv_TinNhan = root.findViewById(R.id.rvTinNhan);
    }
}
