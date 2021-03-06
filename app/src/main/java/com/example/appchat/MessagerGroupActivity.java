package com.example.appchat;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat.Adapter.MessagerGroupAdapter;
import com.example.appchat.Model.GroupChat;
import com.example.appchat.Model.MessegerGroupChat;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

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

    // Image
    private Uri fileUri;
    private String myUrl;
    private StorageTask storageTask;

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

        getInformationGroup();

        // Config Adapter
        arrMGC   = new ArrayList<>();


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
        hmMessengerGroup.put("image","");
        RefMessenger.setValue(hmMessengerGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    etContentGroup.setText("");
                    messagerGroupAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void LoadMessengerGroup(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        rv_List_Messenger_Group.hasFixedSize();
        rv_List_Messenger_Group.setLayoutManager(layoutManager);


        DatabaseReference RefLoadMessengerGroup = fDatabase.getReference("Groups").child(itemGroup.getId()).child("Messengers");
        RefLoadMessengerGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrMGC.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        MessegerGroupChat mgc = dataSnapshot.getValue(MessegerGroupChat.class);
                        arrMGC.add(mgc);
                    }
                messagerGroupAdapter = new MessagerGroupAdapter(MessagerGroupActivity.this, arrMGC, itemGroup.getId());
                rv_List_Messenger_Group.setAdapter(messagerGroupAdapter);
                   // messagerGroupAdapter.notifyDataSetChanged();
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

        // Event Send Image
        ibSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSendImage = new Intent();
                intentSendImage.setAction(Intent.ACTION_GET_CONTENT);
                intentSendImage.setType("image/*");
                startActivityForResult(intentSendImage.createChooser(intentSendImage, "Select Image"), 438);
            }
        });

        // Event Upload Avatar group
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUploadAvatarGroup = new Intent();
                intentUploadAvatarGroup.setAction(Intent.ACTION_GET_CONTENT);
                intentUploadAvatarGroup.setType("image/*");
                startActivityForResult(intentUploadAvatarGroup.createChooser(intentUploadAvatarGroup, "Select Image"), 448);
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
    
    // get Image
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 438 && resultCode ==RESULT_OK && data !=null && data.getData() !=null ){
            fileUri = data.getData();
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads").child(itemGroup.getId()).child(System.currentTimeMillis()+"."+getFileExtension(fileUri));
            storageTask = storageReference.putFile(fileUri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                       throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();
                        DatabaseReference RefMessengerGroup = fDatabase.getReference("Groups").child(itemGroup.getId()).child("Messengers");
                        DatabaseReference RefMessenger = RefMessengerGroup.push();
                        String key = RefMessenger.getKey();
                        HashMap<String,Object> hmMessengerGroup = new HashMap<>();
                        hmMessengerGroup.put("id", key);
                        hmMessengerGroup.put("sender", fUser.getUid());
                        hmMessengerGroup.put("content", etContentGroup.getText().toString());
                        hmMessengerGroup.put("image",myUrl);
                        RefMessenger.setValue(hmMessengerGroup);

                    }
                }
            });
        }
        if(requestCode == 448 && resultCode == RESULT_OK && data !=null && data.getData() != null){
            fileUri = data.getData();
            final StorageReference storageReferenceAvatar = FirebaseStorage.getInstance().getReference("uploads").child(itemGroup.getId()).child("avatar").child(System.currentTimeMillis()+"."+getFileExtension(fileUri));
            storageTask = storageReferenceAvatar.putFile(fileUri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if( ! task.isSuccessful()){
                        throw  task.getException();
                    }
                    return storageReferenceAvatar.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();
                        DatabaseReference RefUpdateAvatarGroup = FirebaseDatabase.getInstance().getReference("Groups").child(itemGroup.getId());
                        HashMap<String,Object> hmUpdateAvatarGroup = new HashMap<>();
                        hmUpdateAvatarGroup.put("avatar", myUrl);

                        RefUpdateAvatarGroup.updateChildren(hmUpdateAvatarGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Glide.with(MessagerGroupActivity.this).load(myUrl).into(ivAvatar);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        
        switch(item.getItemId()){
            case R.id.menu_edit_name_group:
                EditNameGroup();
                break;
            case R.id.menu_add_user_group:

                break;
            case R.id.menu_delete_group:
                break;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void EditNameGroup(){
        String NameGroupOld = tvNameGroup.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Name Group");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        input.setHint("Name group...");
        input.setText(NameGroupOld);
        builder.setView(input);


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                final String NameGroupNew = input.getText().toString();
                DatabaseReference RefEditNameGroup = FirebaseDatabase.getInstance().getReference("Groups").child(itemGroup.getId());
                HashMap<String,Object> hmEditNameGroup = new HashMap<>();
                hmEditNameGroup.put("name", NameGroupNew);
                RefEditNameGroup.updateChildren(hmEditNameGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            tvNameGroup.setText(NameGroupNew);
                            dialog.cancel();
                        }
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
        builder.show();
    }


}