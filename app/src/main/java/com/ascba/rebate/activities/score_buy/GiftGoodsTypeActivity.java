package com.ascba.rebate.activities.score_buy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.GiftShopAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.GoodsType;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;
import java.util.List;

public class GiftGoodsTypeActivity extends BaseDefaultNetActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int cateId;
    private List<GoodsType> goodsTypes;

    @Override
    protected int bindLayout() {
        return R.layout.activity_gift_goods_type;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        tabLayout = fv(R.id.tab_layout);
        viewPager = fv(R.id.viewPager);
        getParams();
        requestType();
    }

    private void getParams() {
        Intent intent = getIntent();
        cateId = intent.getIntExtra("cate_id", -200);
    }

    public static void start(Context context, int cateId) {
        Intent intent = new Intent(context, GiftGoodsTypeActivity.class);
        intent.putExtra("cate_id", cateId);
        context.startActivity(intent);
    }

    private void requestType() {
        AbstractRequest request = buildRequest(UrlUtils.scoregoodsCate, RequestMethod.GET, null);
        executeNetwork(0, request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            String data = (String) result.getData();
            JSONObject dataObj = JSON.parseObject(data);
            String goodsCate = dataObj.getString("goods_cate");
            goodsTypes = JSON.parseArray(goodsCate, GoodsType.class);
            if (goodsTypes != null && goodsTypes.size() > 0) {
                String[] titles = new String[goodsTypes.size()];
                List<Fragment> fragmentList = new ArrayList<>();
                for (int i = 0; i < goodsTypes.size(); i++) {
                    GoodsType goodsType = goodsTypes.get(i);
                    titles[i] = goodsType.getCate_title();
                    tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
                    fragmentList.add(GoodsListFragment.getInstance(goodsType.getCate_id()));
                }
                GiftShopAdapter adapter = new GiftShopAdapter(getSupportFragmentManager(), fragmentList, titles);
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
                setSelectTab();
            }

        }
    }

    private void setSelectTab() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tabAt = tabLayout.getTabAt(i);
            if (goodsTypes.get(i).getCate_id() == cateId) {
                tabAt.select();
                break;
            }
        }
    }

    @Override
    protected void mHandleNoNetwork(int what) {
        super.mHandleNoNetwork(what);
        finishAct(what);
    }

    @Override
    protected void mHandleReLogin(int what, Result result) {
        super.mHandleReLogin(what, result);
        finishAct(what);
    }

    @Override
    protected void mHandleFailed(int what) {
        super.mHandleFailed(what);
        finishAct(what);
    }

    @Override
    protected void mHandle404(int what, Result result) {
        super.mHandle404(what, result);
        finishAct(what);
    }

    /**
     * 分类接口请求失败，关闭界面
     */
    private void finishAct(int what) {
        if (what == 0) {
            finish();
        }
    }
}
