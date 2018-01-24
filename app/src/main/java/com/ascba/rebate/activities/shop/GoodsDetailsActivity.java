package com.ascba.rebate.activities.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.RadioGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.fragments.shop.ShopHomeFragment;

/**
 * Created by Jero on 2018/1/19 0019.
 */

public class ShopActivity extends BaseDefaultNetActivity {

    private String fragmentTag;
    private final String tagHome = "Home";
    private final String tagTypes = "Types";
    private final String tagMine = "Mine";

    @Override
    protected int bindLayout() {
        return R.layout.activity_shop;
    }

    @Override
    protected int setUIMode() {
        return BaseUIActivity.UIMODE_TRANSPARENT_NOTALL;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        ((RadioGroup) fv(R.id.shop_tab_lat)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.shop_home_tab) {
                    changeFragment(tagHome);
                } else if (checkedId == R.id.shop_types_tab) {
                    changeFragment(tagTypes);
                } else if (checkedId == R.id.shop_me_tab) {
                    changeFragment(tagMine);
                }
            }
        });
        changeFragment(tagHome);
    }

    private void changeFragment(String newTag) {
        if (newTag.equals(fragmentTag))
            return;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.show(getFragmentByTag(manager, transaction, newTag));
        if (!TextUtils.isEmpty(fragmentTag))
            transaction.hide(getFragmentByTag(manager, transaction, fragmentTag));
        transaction.commit();
        fragmentTag = newTag;
    }

    private Fragment getFragmentByTag(FragmentManager manager, FragmentTransaction transaction, String tag) {
        Fragment fragment = manager.findFragmentByTag(tag);
        if (null == fragment) {
            fragment = newFragment(tag);
            transaction.add(R.id.fragment_lat, fragment, tag);
        } else {
            // do something
        }
        return fragment;
    }

    private Fragment newFragment(String tag) {
        Fragment fragment;
        switch (tag) {
            case tagHome:
                fragment = new ShopHomeFragment();
                break;
            case tagTypes:
                fragment = new Fragment();
                break;
            case tagMine:
                fragment = new Fragment();
                break;
            default:
                fragment = new Fragment();
                break;
        }
        return fragment;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
