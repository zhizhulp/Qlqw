package com.ascba.rebate.fragments.shop;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.ascba.rebate.utils.DrawableChangeUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.WindowsUtils;
import com.youth.banner.Banner;

/**
 * Created by Jero on 2018/1/19 0019.
 */

public class ShopHomeFragment extends BaseDefaultNetFragment implements View.OnClickListener {

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
        initViewPager();

        fv(R.id.tab_types).setOnClickListener(this);
        fv(R.id.shop_types_bg).setOnClickListener(this);
        fv(R.id.shop_types_bottom).setOnClickListener(this);
        backTv.setOnClickListener(this);
        searchTv.setOnClickListener(this);
        msgIm.setOnClickListener(this);
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
            private int oldType = 0;//0 原始  1 最小范围内  2渐变中  3渐变中黑  4渐变完成

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int absOff = Math.abs(verticalOffset);
                if (absOff <= MIN_ALPHA) {
                    if (oldType != 1) {
                        oldType = 1;
                        titleTop.setBackgroundColor(getResources().getColor(R.color.transparent));
                        setTitleAlpha(0);
                    }
                } else if (absOff <= alphaHeight * 4 / 5) {
                    if (oldType != 2) {
                        oldType = 2;
                        titleTop.setBackgroundColor(getResources().getColor(R.color.grey_black_tv));
                        setTitleWhite();
                    }
                    setTitleAlpha((absOff - MIN_ALPHA) * 255 / (alphaHeight - MIN_ALPHA));
                } else if (absOff <= alphaHeight) {
                    if (oldType != 3) {
                        oldType = 3;
                        setBlackWhile();
                    }
                    setTitleAlpha((absOff - MIN_ALPHA) * 255 / (alphaHeight - MIN_ALPHA));
                } else {
                    if (oldType != 4) {
                        oldType = 4;
                        setTitleAlpha(255);
                    }
                }
            }
        });
    }

    private void setTitleWhite() {
        backTv.setTextColor(getResources().getColor(R.color.white));
        DrawableChangeUtils.setChangeCompoundDrawable(backTv, DrawableChangeUtils.Drawable_LEFT, R.mipmap.back_small_white);
        searchTv.setBackgroundResource(R.drawable.shop_home_search_bg);
        msgIm.setImageResource(R.mipmap.shop_msg);
    }

    private void setBlackWhile() {
        backTv.setTextColor(getResources().getColor(R.color.grey_black_tv2));
        DrawableChangeUtils.setChangeCompoundDrawable(backTv, DrawableChangeUtils.Drawable_LEFT, R.mipmap.back_small);
        searchTv.setBackgroundResource(R.drawable.shop_search_bg);
        msgIm.setImageResource(R.mipmap.shop_msg_grey);
    }

    private void setTitleAlpha(int alpha) {
        titleLat.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
    }

    private void initViewPager() {

        viewPager = fv(R.id.viewpager);
        tabLayout = fv(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "推荐";
            }

            @Override
            public Fragment getItem(int position) {
                return new Fragment();
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                appBarLayout.setExpanded(false);
                Log.i(TAG, "onTabSelected: " + tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i(TAG, "onTabUnselected: ");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i(TAG, "onTabReselected: ");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.shop_back_tv) {
            getActivity().finish();
        } else if (v.getId() == R.id.shop_search_tv) {

        } else if (v.getId() == R.id.shop_msg_im) {

        } else if (v.getId() == R.id.tab_types) {
            appBarLayout.setExpanded(false);
        }
//        else if (v.getId() == R.id.shop_types_bg){
//
//        }else if (v.getId() == R.id.shop_types_bottom){
//
//        }
    }
}
