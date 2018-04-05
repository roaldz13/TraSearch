package com.example.taquio.trasearch.Guest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.taquio.trasearch.BusinessProfile.SectionsPageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Del Mar on 3/17/2018.
 */

public class SectPageAdapter extends SectionsPageAdapter {

    private static final String TAG = "SectPageAdapter";
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public SectPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

}
