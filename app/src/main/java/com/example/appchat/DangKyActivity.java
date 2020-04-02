package com.example.appchat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class DangKyActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private MaterialEditText username, email, password;
    private Button btnDangKy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AnhXa();

        mAuth = FirebaseAuth.getInstance();


        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if(!fcvalidateForm()){
                    return;
                }
                fcregister(txt_username, txt_email, txt_password);
            }
        });

    }


    private void fcregister(final String username, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful()){

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if( task.isSuccessful() ){
                                        Intent intentMain = new Intent(DangKyActivity.this, MainActivity.class);
                                        intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intentMain);
                                        finish();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(DangKyActivity.this, "Bạn không thể đăng ký với email hoặc mật khẩu này", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean fcvalidateForm(){
        if(TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())){
            Toast.makeText(this, "bạn nhập thiếu 1 số thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }else if(password.length() < 6){
            Toast.makeText(this, "mặt khẩu bạn nhập quá ngắn", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void AnhXa(){
        username = findViewById(R.id.metusername);
        email = findViewById(R.id.metemail);
        password =findViewById(R.id.metpassword);
        btnDangKy = findViewById(R.id.btnDangKy);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }
}
