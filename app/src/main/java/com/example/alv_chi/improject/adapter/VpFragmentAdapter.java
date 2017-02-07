package com.example.alv_chi.improject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Alv_chi on 2017/1/14.
 */

public class VpFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public VpFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments ==null?null: mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments ==null?0: mFragments.size();
    }

}
