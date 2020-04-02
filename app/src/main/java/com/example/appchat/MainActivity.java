package com.example.appchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.appchat.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabs;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ToolBar
       Toolbar toolbar = findViewById(R.id.Toolbar);
        toolbar.setTitle("App Chat");
        setSupportActionBar(toolbar);

       // Tab view Page
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        //setupTabIcon();

        mAuth = FirebaseAuth.getInstance();
    }

    private void setupTabIcon(){
        tabs.getTabAt(0).setIcon(R.drawable.tinnhan);
        tabs.getTabAt(1).setIcon(R.drawable.nhom);
        tabs.getTabAt(2).setIcon(R.drawable.danhba);
        tabs.getTabAt(3).setIcon(R.drawable.thongtin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.mDangXuat:
                mAuth.signOut();
                Intent intentMain = new Intent(MainActivity.this, StartActivity.class);
                startActivity(intentMain);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}