package com.yunbao.phonelive.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> tabs;

    public TabAdapter(FragmentManager fm,
                      List<String> tabs,
                      List<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
        this.tabs=tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }
}




























