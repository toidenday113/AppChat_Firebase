package com.example.appchat;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.appchat.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

}