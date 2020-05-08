package com.example.appchat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat.Adapter.MessagerAdapter;
import com.example.appchat.Model.Chat;
import com.example.appchat.Model.User;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagerActivity extends AppCompatActivity {


    private CircleImageView _iv_Profile_Image;
    private TextView _tvUsername;
    private Toolbar _toolbar;
    private ImageButton _btn_Send, _btn_SendImage;
    private EditText _et_ContentSend;
    private RecyclerView _rv_Messager;

    private FirebaseUser _fuser;
    private DatabaseReference reference;

    private Intent intent;
    private  String userid;

    private List<Chat> _mChat;
    private MessagerAdapter messagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager);

        intent = getIntent();
        userid = intent.getStringExtra("userid");
        AnhXa();
        FC_ToolBarChat();

        _btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = _et_ContentSend.getText().toString();
                if(!msg.equals("")){
                    FC_SendMessager(_fuser.getUid(), userid, msg);
                    _btn_SendImage.setVisibility(View.VISIBLE);
                    _et_ContentSend.setText("");
                }
            }
        });

        FC_LoadProfile();
        FC_CheckUserInputContent();
    }

    private void FC_ReadMessageChat(final String myid, final String userid, final String imagesurl){
        _rv_Messager.setHasFixedSize(true);
        
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true); // các item từ dưới đi lên
        _rv_Messager.setLayoutManager(linearLayoutManager);

        _mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _mChat.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat  = snapshot.getValue(Chat.class);

                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                        chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        _mChat.add(chat);
                    }

                    messagerAdapter = new MessagerAdapter(MessagerActivity.this, _mChat, imagesurl );
                    _rv_Messager.setAdapter(messagerAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void FC_CheckUserInputContent(){
        _et_ContentSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String msg = _et_ContentSend.getText().toString();
                if(!msg.equals("")){
                    _btn_SendImage.setVisibility(View.GONE);
                }else{
                    _btn_SendImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void FC_SendMessager(String sender, String receiver, String message){
        DatabaseReference reference_send = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference_send.child("Chats").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if( !task.isSuccessful()){
                    Toast.makeText(MessagerActivity.this, "tin nhấn chưa được gửi!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void FC_LoadProfile(){
        _fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                if(!u.getImageURL().equals("default")){
                    Glide.with(MessagerActivity.this).load(u.getImageURL()).into(_iv_Profile_Image);
                }
                _tvUsername.setText(u.getUsername());

                FC_ReadMessageChat(_fuser.getUid(), userid, u.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FC_ToolBarChat(){
        setSupportActionBar(_toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa(){
        _iv_Profile_Image = findViewById(R.id.ivavatarchat);
        _tvUsername = findViewById(R.id.title);
        _toolbar = findViewById(R.id.toolbarchat);
        _btn_Send = findViewById(R.id.btn_send);
        _et_ContentSend = findViewById(R.id.et_ContentSend);
        _btn_SendImage = findViewById(R.id.btn_sendImage);
        _rv_Messager = findViewById(R.id.rv_ContentChat);
    }
}
