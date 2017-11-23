package com.ascba.rebate.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by 李平 on 2017/9/11 16:52
 * Describe:
 */
public class BalancePagerAdapter extends FragmentStatePagerAdapter {
    private String[] titles;
    private List<Fragment> mFragments;

    public BalancePagerAdapter(FragmentManager fm, String[] titles, List<Fragment> mFragments) {
        super(fm);
        this.titles = titles;
        this.mFragments = mFragments;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
