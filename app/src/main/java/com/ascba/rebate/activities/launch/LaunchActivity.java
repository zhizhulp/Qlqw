package com.ascba.rebate.activities.launch;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.guide.GuideActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.main.MainActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;

/**
 * Created by 李平 on 2017/10/28 16:39
 * Describe:启动页
 */

public class LaunchActivity extends BaseDefaultNetActivity implements Runnable {

    private ImageView image;
    private Handler handler;

    @Override
    protected int bindLayout() {
        return R.layout.activity_launch;
    }

    @Override
    protected int setUIMode() {
        return BaseUIActivity.UIMODE_FULLSCREEN;
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        AppConfig.getInstance().putBoolean("need_show_update", true);
        image = fv(R.id.start_image);
        handler = new Handler();
        requestData();
        requestTime();
    }

    private void requestTime() {
        AbstractRequest request = buildRequest(UrlUtils.time, RequestMethod.GET, null);
        executeNetwork(1, request);
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.startPic, RequestMethod.GET, null);
        executeNetwork(0, request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        //super.mHandle200(what, result);
        if (what == 0) {
            String data = (String) result.getData();
            JSONObject dataObj = JSON.parseObject(data);
            String url = dataObj.getString("url");
            AppConfig.getInstance().putString("launch_icon", url);
            Picasso.with(this).load(url)
//                    .networkPolicy(NetworkPolicy.NO_CACHE)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.mipmap.launch)
                    .error(R.mipmap.launch)
                    .into(image, new Callback() {
                        @Override
                        public void onSuccess() {
                            handler.postDelayed(LaunchActivity.this, 3000);
                        }

                        @Override
                        public void onError() {
                            handler.postDelayed(LaunchActivity.this, 3000);
                        }
                    });
        } else if (what == 1) {
            String data = (String) result.getData();
            JSONObject dataObj = JSON.parseObject(data);
            Long timeserver = dataObj.getLong("time");
            long diff = timeserver - (System.currentTimeMillis() / 1000);
            Log.d(TAG, "mHandle200: " + diff);
            AppConfig.getInstance().putLong("time_diff", diff);
        }
    }

    @Override
    protected void mHandle404(int what, Result result) {
        //super.mHandle404(what, result);
    }

    @Override
    protected void mHandleReLogin(int what, Result result) {
        //super.mHandleReLogin(what, result);
    }

    @Override
    protected void mHandleFailed(int what) {
        //super.mHandleFailed(what);
    }

    @Override
    protected void mHandleNoNetwork(int what) {
        //super.mHandleNoNetwork(what);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(this);
    }

    @Override
    public void run() {
        if (AppConfig.getInstance().getBoolean("need_guide", true)) {
            startActivity(GuideActivity.class, null);
        } else {
            if (AppConfig.getInstance().getBoolean("first_login", true)) {
                startActivity(LoginActivity.class, null);
            } else {
                startActivity(MainActivity.class, null);
            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
