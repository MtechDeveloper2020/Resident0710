package com.mtech.resident.deliverydialog;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAdapter extends FragmentPagerAdapter {
    List<Fragment> mFragmentCollection = new ArrayList<>();
    List<String> mTitleCollection = new ArrayList<>();
    public DeliveryAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addFragment(String title, Fragment fragment)
    {
        mTitleCollection.add(title);
        mFragmentCollection.add(fragment);
    }
    //Needed for
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleCollection.get(position);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentCollection.get(position);
    }
    @Override
    public int getCount() {
        return mFragmentCollection.size();
    }
}