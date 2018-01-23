package com.ascba.rebate.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */
public class GoodsDetPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> data;
    private String [] titles;

    public GoodsDetPagerAdapter(FragmentManager fm,List<Fragment> data, String[] titles) {
        super(fm);
        this.data = data;
        this.titles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
