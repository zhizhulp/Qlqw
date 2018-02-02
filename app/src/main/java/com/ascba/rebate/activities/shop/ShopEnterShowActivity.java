package com.ascba.rebate.activities.shop;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.fragments.shop.ShopEnterFragment;

/**
 * Created by Jero on 2018/1/25 0025.
 */

public class ShopEnterShowActivity extends BaseDefaultNetActivity {
    private ShopEnterFragment enterFragment;

    @Override
    protected int bindLayout() {
        return R.layout.activity_shop_enter;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mMoneyBar.setTextTitle("查看权益");
        changeEnter();
        enterFragment.setShowType();
    }

    private void changeEnter() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (enterFragment == null) {
            enterFragment = new ShopEnterFragment();
            fragmentTransaction.add(R.id.fragment_lat, enterFragment);
        } else {
            fragmentTransaction.show(enterFragment);
        }
        fragmentTransaction.commit();
    }
}
