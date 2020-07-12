package com.example.appchat.TabPage;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Adapter.GroupAdapter;
import com.example.appchat.CreateGroupBottomSheetDialog;
import com.example.appchat.Model.GroupChat;
import com.example.appchat.Model.mGroup;
import com.example.appchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NhomFragment extends Fragment {

    private FloatingActionButton fabAddGroup;
    private RecyclerView rvGroup;
    private String textGroup;

    private FirebaseUser fUser;
    private FirebaseAuth fAuth;
    private FirebaseDatabase fDatabase;

    private GroupAdapter groupAdapter;
    private List<GroupChat> arrGroup;
    private List<mGroup> arrMGroup;

    private View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        root = inflater.inflate(R.layout.fragment_nhom, container, false);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance();

        arrGroup = new ArrayList<>();
        arrMGroup = new ArrayList<>();


        MapObject();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvGroup.setLayoutManager(layoutManager);

        groupAdapter = new GroupAdapter(getContext(), arrGroup, arrMGroup);
        rvGroup.setAdapter(groupAdapter);


        LoadListGroup();

        fabAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  CreateGroup().show();
                CreateGroupBottomSheetDialog createGroupBottomSheetDialog = new CreateGroupBottomSheetDialog();
                createGroupBottomSheetDialog.show(getParentFragmentManager(),"BottomSheet");
            }
        });


        return root;
    }

    private void MapObject(){
        fabAddGroup = root.findViewById(R.id.floatingActionButton_AddGroup);
        rvGroup = root.findViewById(R.id.recyclerview_ListGroup);
    }

    private AlertDialog.Builder CreateGroup(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Create Group");

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        input.setHint("Name group...");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textGroup = input.getText().toString();
               
                DatabaseReference dRefRootGroup = fDatabase.getReference("Groups");
                DatabaseReference dRefGroup = dRefRootGroup.push();
                final String key = dRefGroup.getKey();

                HashMap<String,Object> hmGroup = new HashMap<>();
                hmGroup.put("id", key);
                hmGroup.put("name", textGroup);
                hmGroup.put("avatar", "default");
                hmGroup.put("admin", fUser.getUid());
                
                dRefGroup.setValue(hmGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                         
                        if(task.isSuccessful()) {
                             DatabaseReference dRefRootUser = fDatabase.getReference("Users").child(fUser.getUid()).child("mGroup");
                             HashMap<String, Object> hmmGroup = new HashMap<>();
                             hmmGroup.put("idGroup", key);
                             dRefRootUser.push().setValue(hmmGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "Tạo Nhóm Thành Công", Toast.LENGTH_SHORT).show();
                                    }
                                 }
                             });
                         }//
                        
                    }
                });


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder;
    }

    private void LoadListGroup(){
        DatabaseReference RefUserGroup = fDatabase.getReference("Users").child(fUser.getUid()).child("mGroup");
        RefUserGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrMGroup.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mGroup mgroup = new mGroup(snapshot.getKey(), snapshot.child("idGroup").getValue().toString());
                    arrMGroup.add(mgroup);
                }

                DatabaseReference RefGroup = fDatabase.getReference("Groups");
                RefGroup.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrGroup.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            for( mGroup member : arrMGroup){
                                if( member.getIdGroup().equals(snapshot.getKey()) ){
                                    GroupChat groupChat = snapshot.getValue(GroupChat.class);
                                    arrGroup.add(groupChat);
                                }
                            }

                        }

//                        groupAdapter = new GroupAdapter(getContext(), arrGroup, arrMGroup);
//                        rvGroup.setAdapter(groupAdapter);
                        groupAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
