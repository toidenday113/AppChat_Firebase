package com.example.appchat;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Adapter.UserAddGroupAdapter;
import com.example.appchat.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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

public class CreateGroupBottomSheetDialog  extends BottomSheetDialogFragment {
    private BottomSheetBehavior mBehavior;
    private EditText etNameGroup;
    private ImageButton ibCreateGroup;
    private RecyclerView rvListUser;
    private TextView tv_huy_create_group;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dRef;

    private UserAddGroupAdapter userAddGroupAdapter;
    private List<User> arrListUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(),R.layout.layout_bottom_sheet_create_group, null);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height =getFullScreen();
        linearLayout.setLayoutParams(params);
        bottomSheetDialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from( (View) view.getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        MapObject(view);

        // Set EditText
        etNameGroup.setWidth((int) ((Resources.getSystem().getDisplayMetrics().widthPixels)/1.5));
        // Firebase
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance();

        arrListUser = new ArrayList<>();
        rvListUser = view.findViewById(R.id.RecyclerView_List_User_Add_Group);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        rvListUser.setLayoutManager(layoutManager);


        LoadUserJoinGroup();

        // Event
        ibCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateGroup();
            }
        });
        tv_huy_create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        return bottomSheetDialog;
    }

    public static int getFullScreen(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    private void MapObject(View v){
        etNameGroup = v.findViewById(R.id.EditText_Name_Group);
        ibCreateGroup = v.findViewById(R.id.ImageButton_Create_Group);
        tv_huy_create_group = v.findViewById(R.id.textView_Huy);
    }
    private void LoadUserJoinGroup(){

        dRef = fDatabase.getReference("Users");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User u = snapshot.getValue(User.class);
                    if(! u.getId().equals(fUser.getUid())){
                        arrListUser.add(u);
                    }
                }
               // userAddGroupAdapter.notifyDataSetChanged();
                userAddGroupAdapter = new UserAddGroupAdapter(getContext(), arrListUser);
                rvListUser.setAdapter(userAddGroupAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CreateGroup(){

        if(userAddGroupAdapter.getArrUserJoinGroup().size() > 0){
            //Toast.makeText(getContext(), userAddGroupAdapter.getArrUserJoinGroup().get(0), Toast.LENGTH_SHORT).show();
            DatabaseReference dRefRootGroup = fDatabase.getReference("Groups");
            DatabaseReference dRefGroup = dRefRootGroup.push();
            final String key = dRefGroup.getKey();

            HashMap<String,Object> hmGroup = new HashMap<>();
            hmGroup.put("id", key);
            hmGroup.put("name", etNameGroup.getText().toString());
            hmGroup.put("avatar", "default");
            hmGroup.put("admin", fUser.getUid());

            dRefGroup.setValue(hmGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()) {
                        for(String id : userAddGroupAdapter.getArrUserJoinGroup()){
                            DatabaseReference dRefJoinUser = fDatabase.getReference("Users").child(id).child("mGroup");
                            HashMap<String, Object> IdGroup = new HashMap<>();
                            IdGroup.put("idGroup", key);
                            dRefJoinUser.push().setValue(IdGroup);

                        }
                        DatabaseReference dRefRootUser = fDatabase.getReference("Users").child(fUser.getUid()).child("mGroup");
                        HashMap<String, Object> hmmGroup = new HashMap<>();
                        hmmGroup.put("idGroup", key);
                        dRefRootUser.push().setValue(hmmGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    //Toast.makeText(getActivity(), "Tạo Nhóm Thành Công", Toast.LENGTH_SHORT).show();
                                   mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                }
                            }
                        });
                    }//

                }
            });



        }
        else{
            DatabaseReference dRefRootGroup = fDatabase.getReference("Groups");
            DatabaseReference dRefGroup = dRefRootGroup.push();
            final String key = dRefGroup.getKey();

            HashMap<String,Object> hmGroup = new HashMap<>();
            hmGroup.put("id", key);
            hmGroup.put("name", etNameGroup.getText().toString());
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
                                    mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                }
                            }
                        });
                    }//

                }
            });


        }
    }

}
