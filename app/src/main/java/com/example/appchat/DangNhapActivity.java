package com.example.appchat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

    private MaterialEditText _metemail, _metpassword;
    private Button _btnDangNhap;
    private FirebaseAuth mAuth;
    private TextView _tvQuenMatKhau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        // ToolBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AnhXa();
        mAuth = FirebaseAuth.getInstance();

        FC_DangNhap();

    } // End onCreate

    private void FC_QuenMatKhau(){
        _tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void FC_DangNhap(){
        _btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fc_validateForm()){
                    return;
                }
                String txt_email = _metemail.getText().toString();
                String txt_password = _metpassword.getText().toString();

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

    private void FC_ResetMatKhau(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Khôi phục mật khẩu");

        View customlayout = getLayoutInflater().inflate(R.layout.custom_alertdialog, null);
        builder.setView(customlayout);
    }
    private void AnhXa(){
        _btnDangNhap = findViewById(R.id.btnDangNhap);
        _metemail = findViewById(R.id.metemail);
        _metpassword = findViewById(R.id.metpassword);
        _tvQuenMatKhau = findViewById(R.id.tvQuenMatKhau);
    }

    private boolean fc_validateForm(){
        if(TextUtils.isEmpty(_metemail.getText().toString())|| TextUtils.isEmpty(_metpassword.getText().toString()) ){
            Toast.makeText(this, "Bạn chưa nhập Email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
