package com.ascba.rebate.fragments.shop;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.WindowsUtils;
import com.youth.banner.Banner;

/**
 * Created by Jero on 2018/1/19 0019.
 */

public class ShopHomeFragment extends BaseDefaultNetFragment {

    private AppBarLayout appBarLayout;
    private Banner banner;

    // 标题栏
    private Toolbar toolbar;
    private View titleTop;
    private RelativeLayout titleLat;
    private TextView backTv, searchTv;
    private ImageView msgIm;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override

    protected int bindLayout() {
        return R.layout.fragment_shop_home;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initChildHeight();

        tabLayout = fv(R.id.tab_layout);
        viewPager = fv(R.id.viewpager);
        tabLayout.addTab(tabLayout.newTab().setText("推荐"));
        tabLayout.addTab(tabLayout.newTab().setText("男装"));
        tabLayout.addTab(tabLayout.newTab().setText("电器"));
        tabLayout.addTab(tabLayout.newTab().setText("x"));
        tabLayout.addTab(tabLayout.newTab().setText("xzou78"));
        tabLayout.addTab(tabLayout.newTab().setText("78"));
        tabLayout.addTab(tabLayout.newTab().setText("ou78适度"));
        tabLayout.addTab(tabLayout.newTab().setText("适度分红款"));
        tabLayout.addTab(tabLayout.newTab().setText("度分红款"));
        tabLayout.addTab(tabLayout.newTab().setText("度分红"));
        tabLayout.addTab(tabLayout.newTab().setText("分红"));
        tabLayout.addTab(tabLayout.newTab().setText("家居"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                appBarLayout.setExpanded(false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fv(R.id.tab_types).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        fv(R.id.shop_types_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: shop_types_bg");
            }
        });
        fv(R.id.shop_types_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: shop_types_bottom");
            }
        });
        fv(R.id.shop_back_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: shop_back_tv");
            }
        });
        fv(R.id.shop_msg_im).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: shop_msg_im");
            }
        });

    }

    private void initChildHeight() {

        banner = fv(R.id.banner);
        LinearLayout.LayoutParams bannerParams = (LinearLayout.LayoutParams) banner.getLayoutParams();
        bannerParams.height = WindowsUtils.getWindowsWidth(getContext()) * 5 / 9;

        appBarLayout = fv(R.id.app_bar_lat);
        int statusBar = WindowsUtils.getStatusBarHeight(getContext());
        toolbar = fv(R.id.toolbar);
        FrameLayout.LayoutParams toolBatParams = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        toolBatParams.height = statusBar + ((int) ScreenDpiUtils.dp2px(getContext(), 48));
        final int alphaHeight = bannerParams.height - toolBatParams.height;
        titleTop = fv(R.id.title_top);
        LinearLayout.LayoutParams topParams = (LinearLayout.LayoutParams) titleTop.getLayoutParams();
        topParams.height = statusBar;
        titleLat = fv(R.id.title_lat);
        backTv = fv(R.id.shop_back_tv);
        searchTv = fv(R.id.shop_search_tv);
        msgIm = fv(R.id.shop_msg_im);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            private int MIN_ALPHA = ((int) ScreenDpiUtils.dp2px(getContext(), 10));
            private int oldType = 0;//0 原始  1 最小范围内  2渐变中  3渐变完成

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int absOff = Math.abs(verticalOffset);
                int alpha = 255;
                if (absOff <= MIN_ALPHA) {
                    if (oldType != 1) {
                        oldType = 1;

                    }
                } else if (absOff <= alphaHeight) {
                    alpha = absOff * 255 / alphaHeight;
                } else {

                }
                Log.i(TAG, "onOffsetChanged: " + verticalOffset + "//" + absOff + "//" + alpha);
            }
        });


    }

}
