package com.ascba.rebate.activities.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultPayActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.fragments.shop.ShopEnterFragment;
import com.ascba.rebate.fragments.shop.ShopPayFragment;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by Jero on 2018/1/25 0025.
 */

public class ShopEnterActivity extends BaseDefaultPayActivity {
    private static final int TYPE = 600;
    private int type = 0;// 1 权益  2 

    private ShopPayFragment payFragment;
    private ShopEnterFragment enterFragment;

    @Override
    protected int bindLayout() {
        return R.layout.activity_shop_enter;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        getParams();
        //startActivity(new Intent(this, ShopEnterActivity.class).putExtra("type", 2));
        if (type == 0)
            requestTypeNetwork();
    }

    private void getParams() {
        int param = getIntent().getIntExtra("type", 0);
        if (param == 1) {
            type = 1;
            changeEnter();
        } else if (param == 2) {
            type = 2;
            changePay();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getParams();
    }

    private void changeEnter() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (enterFragment == null) {
            enterFragment = new ShopEnterFragment();
            fragmentTransaction.add(R.id.fragment_lat, enterFragment);
        } else {
            fragmentTransaction.show(enterFragment);
        }
        if (payFragment != null)
            fragmentTransaction.hide(payFragment);
        fragmentTransaction.commit();
    }

    private void changePay() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (payFragment == null) {
            payFragment = new ShopPayFragment();
            payFragment.setBtnClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeEnter();
                }
            });
            fragmentTransaction.add(R.id.fragment_lat, payFragment);
        } else {
            fragmentTransaction.show(payFragment);
        }
        if (enterFragment != null)
            fragmentTransaction.hide(enterFragment);
        fragmentTransaction.commit();
    }

    private void requestTypeNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.getStoreRedirect, RequestMethod.GET, null);
        executeNetwork(TYPE, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == TYPE) {
            type = JSON.parseObject(result.getData().toString()).getIntValue("store_redirect");
            if (type == 1) {
                changeEnter();
            } else {
                changePay();
            }
        }
    }

    @Override
    protected void mHandle404(int what, Result result) {
        super.mHandle404(what, result);
        if (what == TYPE) {
            finish();
        }
    }

    @Override
    protected void mHandleFailed(int what) {
        super.mHandleFailed(what);
        if (what == TYPE) {
            finish();
        }
    }
}
