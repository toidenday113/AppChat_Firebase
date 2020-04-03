package com.example.appchat.ui.main;

import android.content.Context;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.appchat.R;
import com.example.appchat.TabPage.DanhBaFragment;
import com.example.appchat.TabPage.NhomFragment;
import com.example.appchat.TabPage.ThongTinFragment;
import com.example.appchat.TabPage.TinNhanFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
      //  return PlaceholderFragment.newInstance(position + 1);
        Fragment fragment = null;
        switch(position){
            case 0:
                fragment = new TinNhanFragment();
                break;
            case 1:
                fragment = new DanhBaFragment();
                break;
            case 2:
                fragment = new NhomFragment();
                break;
            case 3:
                fragment = new ThongTinFragment();
                break;
        }
        return fragment;
    }


    @Override
    public CharSequence getPageTitle(int position) {
       return mContext.getResources().getString(TAB_TITLES[position]);

    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}