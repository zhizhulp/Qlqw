package com.ascba.rebate.activities.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
    private int type = 0;//0 入驻（默认） 1 权益  2  成功

    private ShopPayFragment payFragment;
    private ShopEnterFragment enterFragment;

    @Override
    protected int bindLayout() {
        return R.layout.activity_shop_enter;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        getParams(getIntent());
        if (type == 0)
            requestTypeNetwork();
    }

    private void getParams(Intent intent) {
        int param = intent.getIntExtra("type", 0);
        if (param == 1) {
            mMoneyBar.setTextTitle("查看权益");
            type = 1;
            changeEnter();
            enterFragment.setShowType();
        } else if (param == 2) {
            changePay();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getParams(intent);
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
        type = 2;
        mMoneyBar.setTextTitle("入驻成功");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (payFragment == null) {
            payFragment = new ShopPayFragment();
            payFragment.setBtnClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo
                    showPayDialog("", "");
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
            if (JSON.parseObject(result.getData().toString()).getIntValue("store_redirect") == 1) {
                mMoneyBar.setTextTitle("商城入驻");
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
