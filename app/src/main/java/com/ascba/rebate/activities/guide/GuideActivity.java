package com.ascba.rebate.activities.guide;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.main.MainActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultUIActivity;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.rd.PageIndicatorView;

/**
 * Created by 李平 on 2017/10/28 15:40
 * Describe: 引导页
 */

public class GuideActivity extends BaseDefaultUIActivity {
    private int[] imgs = new int[]{R.mipmap.guide_01, R.mipmap.guide_02, R.mipmap.guide_03};

    @Override
    protected int bindLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        //让虚拟键盘一直不显示
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
        AppConfig.getInstance().putBoolean("need_guide", false);
        ViewPager viewPager = fv(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgs.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(GuideActivity.this);
                imageView.setImageResource(imgs[position]);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                if (Build.VERSION.SDK_INT >= 19)
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                else
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                container.addView(imageView);
                if (position == imgs.length - 1) {
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (AppConfig.getInstance().getBoolean("first_login", true)) {
                                startActivity(LoginActivity.class, null);
                            } else {
                                startActivity(MainActivity.class, null);
                            }
                            finish();
                        }
                    });
                }
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        PageIndicatorView indicator = fv(R.id.indicator);
        indicator.setViewPager(viewPager);
    }

    @Override
    protected int setUIMode() {
        return BaseUIActivity.UIMODE_FULLSCREEN_NOTALL;
    }
}
