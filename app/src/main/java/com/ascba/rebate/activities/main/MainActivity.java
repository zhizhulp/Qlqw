package com.ascba.rebate.activities.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ascba.rebate.BuildConfig;
import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.fragments.main.HomeFragment;
import com.ascba.rebate.fragments.main.MineFragment;
import com.ascba.rebate.fragments.main.MsgFragment;
import com.ascba.rebate.manager.JpushSetManager;
import com.ascba.rebate.utils.PackageUtils;
import com.ascba.rebate.view.AppTabs;
import com.taobao.sophix.SophixManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends BaseDefaultNetActivity implements AppTabs.Callback {
    private List<Fragment> mFragments = new ArrayList<>();
    private Fragment mHomeFragment;
    private Fragment mMsgFragment;
    private Fragment mMineFragment;

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    protected void initViews(Bundle savedInstanceState) {
        resolveProblems(savedInstanceState);
        super.initViews(savedInstanceState);
        if(!BuildConfig.DEBUG){
            SophixManager.getInstance().queryAndLoadNewPatch();
        }
        initAppTabs();
        initAllFragments();
        selFrgByPos(mHomeFragment);
        setJPushAlias();
    }

    private void setJPushAlias() {
        JpushSetManager jpush = new JpushSetManager(0);
        if (!AppConfig.getInstance().getBoolean("jpush_set_alias_success", false)) {
            jpush.setAlias(AppConfig.getInstance().getString("access_token", null));
        }
        if (!AppConfig.getInstance().getBoolean("jpush_set_tag_success", false)) {
            jpush.setTag(getTag(PackageUtils.isAppDebug(this)));
        }
    }

    //调用JPush API设置Tag
    private Set<String> getTag(boolean appDebug) {
        Set<String> tagSet = new LinkedHashSet<String>();
        if (appDebug) {
            tagSet.add("debug");
        } else {
            tagSet.add("release");
        }
        return tagSet;
    }

    private void initAllFragments() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }
        if (mMsgFragment == null) {
            mMsgFragment = new MsgFragment();
        }
        if (mMineFragment == null) {
            mMineFragment = new MineFragment();
        }

    }

    private void initAppTabs() {
        AppTabs tabs = fv(R.id.tabs);
        tabs.setCallback(this);
    }

    @Override
    public void clickZero(View v) {
        selFrgByPos(mHomeFragment);
    }


    @Override
    public void clickThree(View v) {
        selFrgByPos(mMsgFragment);
    }

    @Override
    public void clickFour(View v) {
        selFrgByPos(mMineFragment);
    }

    //根据位置切换相应碎片
    public void selFrgByPos(Fragment selectFrg) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (selectFrg instanceof HomeFragment) {
            if (!mHomeFragment.isAdded()) {
                ft.add(R.id.fl_change, mHomeFragment);
                mFragments.add(mHomeFragment);
            }
        } else if (selectFrg instanceof MsgFragment) {
            if (!mMsgFragment.isAdded()) {
                ft.add(R.id.fl_change, mMsgFragment);
                mFragments.add(mMsgFragment);
            }
        } else if (selectFrg instanceof MineFragment) {
            if (!mMineFragment.isAdded()) {
                ft.add(R.id.fl_change, mMineFragment);
                mFragments.add(mMineFragment);
            }
        }
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            if (fragment == selectFrg) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

    /**
     * 解决fragment崩溃后重叠的问题
     */
    private void resolveProblems(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (fragments != null && fragments.size() != 0) {
                for (int i = 0; i < fragments.size(); i++) {
                    Fragment fragment = fragments.get(i);
                    if (mHomeFragment == null && fragment instanceof HomeFragment)
                        mHomeFragment = fragment;
                    mFragments.add(fragment);
                    if (mMsgFragment == null && fragment instanceof MsgFragment)
                        mMsgFragment = fragment;
                    mFragments.add(fragment);
                    if (mMineFragment == null && fragment instanceof MineFragment)
                        mMineFragment = fragment;
                    mFragments.add(fragment);
                }
            }
        }
    }
}
