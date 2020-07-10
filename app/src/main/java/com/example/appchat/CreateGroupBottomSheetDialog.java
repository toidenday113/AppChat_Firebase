package com.example.appchat;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Adapter.UserAddGroupAdapter;
import com.example.appchat.Model.User;
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
import java.util.List;

public class CreateGroupBottomSheetDialog  extends BottomSheetDialogFragment {
    private BottomSheetBehavior mBehavior;
    private RecyclerView rvListUser;
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



        arrListUser = new ArrayList<>();

        rvListUser = view.findViewById(R.id.RecyclerView_List_User_Add_Group);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        rvListUser.setLayoutManager(layoutManager);


        LoadUserJoinGroup();

        view.findViewById(R.id.ImageButton_Create_Group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userAddGroupAdapter.getArrUserJoinGroup().size() > 0){
                    Toast.makeText(getContext(), userAddGroupAdapter.getArrUserJoinGroup().get(0), Toast.LENGTH_SHORT).show();
                }
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        return bottomSheetDialog;
    }

    public static int getFullScreen(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void LoadUserJoinGroup(){
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance();

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
}
