package com.ascba.rebate.activities.shop;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.GoodsDetPagerAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.fragments.score_shop.BtmFragment;
import com.ascba.rebate.fragments.shop_goods_details.GDComtFragment;
import com.ascba.rebate.fragments.shop_goods_details.GDDetFragment;
import com.ascba.rebate.manager.ToastManager;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.view.qr.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends BaseDefaultNetActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener {

    private View imSpot;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvTitle;
    private BtmFragment btmFragment;

    @Override
    protected int bindLayout() {
        return R.layout.activity_shop;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        setImSpot(false);
        GoodsDetPagerAdapter adapter = new GoodsDetPagerAdapter(getSupportFragmentManager(),
                addFragment(), new String[]{"商品", "评价", "详情"});
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
    }

    private List<Fragment> addFragment() {
        List<Fragment> data = new ArrayList<>();
        data.add(new GDDetFragment());
        data.add(new GDComtFragment());
        btmFragment = new BtmFragment();
        data.add(btmFragment);
        return data;
    }

    private void initView() {
        //返回
        View tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(this);
        //消息
        View imMsg = findViewById(R.id.im_msg);
        imMsg.setOnClickListener(this);
        //红点
        imSpot = findViewById(R.id.im_spot);
        //tab
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        //title
        tvTitle = findViewById(R.id.tv_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.im_msg:
                ToastManager.show("进入消息界面");
                break;
        }
    }

    //设置小红点的显示与隐藏
    private void setImSpot(boolean visible) {
        imSpot.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 2) {
            btmFragment.initView("http://www.baidu.com");
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //切换title
    public void switchTitle(boolean isOne) {
        if (isOne) {
            hideAnimation(tvTitle);
            showAnimation(tabLayout);
        } else {
            hideAnimation(tabLayout);
            showAnimation(tvTitle);
        }
        tvTitle.setVisibility(isOne ? View.GONE : View.VISIBLE);
        tabLayout.setVisibility(isOne ? View.VISIBLE : View.GONE);
        tabLayout.setSelectedTabIndicatorHeight(isOne ? (int) (ScreenDpiUtils.px2dp(this, 1f)) : 0);
    }

    private void showAnimation(View view) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                view == tvTitle ? 0.7f : -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(400);
        view.startAnimation(animation);
    }

    private void hideAnimation(View view) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                view == tvTitle ? 0.7f : -1.0f);
        animation.setDuration(200);
        view.startAnimation(animation);
    }

}
