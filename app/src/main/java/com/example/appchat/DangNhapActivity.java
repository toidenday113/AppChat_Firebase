package com.example.appchat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.rengwuxian.materialedittext.MaterialEditText;

public class DangNhapActivity extends AppCompatActivity {

    private MaterialEditText metemail, metpassword;
    private Button btnDangNhap;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        AnhXa();
        mAuth = FirebaseAuth.getInstance();
        btnDangNhap.setEnabled(false);

        metpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!TextUtils.isEmpty(metemail.getText().toString()) || !TextUtils.isEmpty(metpassword.getText().toString())){
                    btnDangNhap.setEnabled(true);
                }else{
                    btnDangNhap.setEnabled(false);
                }
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fc_validateForm()){
                    return;
                }
                String txt_email = metemail.getText().toString();
                String txt_password = metpassword.getText().toString();

                mAuth.signInWithEmailAndPassword(txt_email, txt_password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    user.getUid();
                                    Intent intentMain = new Intent(DangNhapActivity.this, MainActivity.class);
                                    intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intentMain);
                                    finish();
                                }else{
                                    Toast.makeText(DangNhapActivity.this, "Email hay mật khẩu đăng nhập không đúng", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    private void AnhXa(){
        btnDangNhap = findViewById(R.id.btnDangNhap);
        metemail = findViewById(R.id.metemail);
        metpassword = findViewById(R.id.metpassword);
    }

    private boolean fc_validateForm(){
        if(TextUtils.isEmpty(metemail.getText().toString())|| TextUtils.isEmpty(metpassword.getText().toString()) ){
            Toast.makeText(this, "Bạn chưa nhập Email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
