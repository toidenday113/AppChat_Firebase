package com.example.appchat;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.appchat.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabs;
    private static final int[] IMAGE_ACTIVE = new int[]{
            R.drawable.ic_tinhan_active,
            R.drawable.ic_danhba_active,
            R.drawable.ic_nhom_active,
            R.drawable.ic_thongtin_active,
    };
    private static final int[] IMAGE_NOTACTIVE = new int[]{
            R.drawable.ic_tinnhan,
            R.drawable.ic_danhba,
            R.drawable.ic_nhom,
            R.drawable.ic_thongtin,
    };

    private FirebaseUser _fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _fuser = FirebaseAuth.getInstance().getCurrentUser();
       //Tab view Page
        tabs = findViewById(R.id.tabs);
        //
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
        setupTabIcon();

        FC_SeclectTabActive();

    }

    private void setupTabIcon(){
        tabs.getTabAt(0).setIcon(R.drawable.ic_tinhan_active);
        tabs.getTabAt(1).setIcon(IMAGE_NOTACTIVE[1]);
        tabs.getTabAt(2).setIcon(IMAGE_NOTACTIVE[2]);
        tabs.getTabAt(3).setIcon(IMAGE_NOTACTIVE[3]);
    }


    private void FC_SeclectTabActive(){
        tabs.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#2000FF"));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int p = tab.getPosition();
                tabs.getTabAt(p).setIcon(IMAGE_ACTIVE[p]);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int p = tab.getPosition();
                tabs.getTabAt(p).setIcon(IMAGE_NOTACTIVE[p]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void FC_Status(String status){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(_fuser.getUid());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FC_Status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //FC_Status("offline");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FC_Status("offline");
    }
}