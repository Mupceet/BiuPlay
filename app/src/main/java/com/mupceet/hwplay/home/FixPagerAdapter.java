package com.mupceet.hwplay.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgz on 1/3/18.
 */

public class FixPagerAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles;
    private List<Fragment> mFragments = new ArrayList<>();

    public void setTitles(String[] titles) {
        mTitles = titles;
    }

    public void setFragments(List<Fragment> fragments) {
        mFragments = fragments;
    }

    public FixPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
