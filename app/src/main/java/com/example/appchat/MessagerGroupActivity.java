package com.example.appchat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat.Adapter.MessagerGroupAdapter;
import com.example.appchat.Model.GroupChat;
import com.example.appchat.Model.MessegerGroupChat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class MessagerGroupActivity extends AppCompatActivity {
    private TextView tvNameGroup;
    private EditText etContentGroup;
    private Toolbar tbGroup;
    private ImageButton ibSendText, ibSendImage;
    private ImageView ivAvatar;
    private GroupChat itemGroup;
    private RecyclerView rv_List_Messenger_Group;
    //Firebase
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseDatabase fDatabase;

    // Adapter
    private List<MessegerGroupChat> arrMGC;
    private MessagerGroupAdapter messagerGroupAdapter;
    private String urlImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager_group);

        MapObject();
        setSupportActionBar(tbGroup);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbGroup.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Load Auth & Database
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance();

        // Config Adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        rv_List_Messenger_Group.setLayoutManager(layoutManager);
        arrMGC   = new ArrayList<>();
        messagerGroupAdapter = new MessagerGroupAdapter(this, arrMGC, urlImage);
        rv_List_Messenger_Group.setAdapter(messagerGroupAdapter);


        getInformationGroup();
        LoadMessengerGroup();
        EventObject();
    }

    private void SendMessengerGroup(){
        DatabaseReference RefMessengerGroup = fDatabase.getReference("Groups").child(itemGroup.getId()).child("Messengers");
        DatabaseReference RefMessenger = RefMessengerGroup.push();
        String key = RefMessenger.getKey();
        HashMap<String,Object> hmMessengerGroup = new HashMap<>();
        hmMessengerGroup.put("id", key);
        hmMessengerGroup.put("sender", fUser.getUid());
        hmMessengerGroup.put("content", etContentGroup.getText().toString());
        RefMessenger.setValue(hmMessengerGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    etContentGroup.setText("");
                }
            }
        });
    }

    private void LoadMessengerGroup(){
        DatabaseReference RefLoadMessengerGroup = fDatabase.getReference("Groups").child(itemGroup.getId()).child("Messengers");
        RefLoadMessengerGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrMGC.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        MessegerGroupChat mgc = dataSnapshot.getValue(MessegerGroupChat.class);
                        arrMGC.add(mgc);
                    }
                    messagerGroupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void EventObject(){
        // Check EditText has input text
        etContentGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String smg = etContentGroup.getText().toString();
                if(!smg.equals("")){
                    ibSendImage.setVisibility(View.GONE);

                }else{
                    ibSendImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Event send messenger group
        ibSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! TextUtils.isEmpty(etContentGroup.getText().toString())){
                    SendMessengerGroup();
                }
            }
        });

    }

    private void getInformationGroup(){
        itemGroup = (GroupChat)getIntent().getSerializableExtra("itemGroup");
        tvNameGroup.setText(itemGroup.getName());
        if(! itemGroup.getAvatar().equals("default")){
            Glide.with(this).load(itemGroup.getAvatar()).into(ivAvatar);
        }
    }

    private void MapObject(){

        tvNameGroup = findViewById(R.id.TextView_Title_Group);
        ibSendText = findViewById(R.id.btn_send_text);
        ibSendImage = findViewById(R.id.btn_send_Image);
        ivAvatar = findViewById(R.id.ImageView_Avatar_Chat);
        etContentGroup = findViewById(R.id.et_Content_Group_Send);
        tbGroup = findViewById(R.id.ToolBar_Chat_Group);
        rv_List_Messenger_Group = findViewById(R.id.rv_Content_Chat_Group);

    }
}