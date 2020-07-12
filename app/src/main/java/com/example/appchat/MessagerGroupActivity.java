package com.example.appchat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.appchat.Model.GroupChat;

public class MessagerGroupActivity extends AppCompatActivity {
    private TextView tvNameGroup;
    private EditText etContentGroup;
    private Toolbar tbGroup;
    private ImageButton ibSendText, ibSendImage;
    private ImageView ivAvatar;
    private String sidGroup;
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


        getInformationGroup();
        EventObject();
    }
    private void EventObject(){
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
    }

    private void getInformationGroup(){
        GroupChat itemGroup = (GroupChat)getIntent().getSerializableExtra("itemGroup");
        tvNameGroup.setText(itemGroup.getName());
        sidGroup = itemGroup.getId();
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

    }
}