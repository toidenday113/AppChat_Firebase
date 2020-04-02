package com.example.appchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    private Button btnSceenDangKy, btnSceenDangNhap;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        AnhXa();
        btnSceenDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDangKy = new Intent(StartActivity.this, DangKyActivity.class);
               // intentDangKy.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentDangKy);
                //finish();
            }
        });

        btnSceenDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDangNhap = new Intent(StartActivity.this, DangNhapActivity.class);
                // intentDangKy.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentDangNhap);
                //finish();
            }
        });

    }

    private void AnhXa(){
        btnSceenDangNhap = findViewById(R.id.btnSceenDangNhap);
        btnSceenDangKy = findViewById(R.id.btnSceenDangKy);
    }

    @Override
    protected void onStart() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!= null){
            Intent intentMain = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intentMain);
            finish();
        }
        super.onStart();
    }
}
