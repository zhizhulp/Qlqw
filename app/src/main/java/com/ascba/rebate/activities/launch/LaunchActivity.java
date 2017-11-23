package com.ascba.rebate.activities.launch;

import android.os.Bundle;
import android.os.Handler;
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
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by 李平 on 2017/10/28 16:39
 * Describe:启动页
 */

public class LaunchActivity extends BaseDefaultNetActivity {

    private ImageView image;

    @Override
    protected int bindLayout() {
        return R.layout.activity_launch;
    }
    @Override
    protected int setUIMode() {
        return BaseUIActivity.UIMODE_FULLSCREEN;
    }
    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        AppConfig.getInstance().putBoolean("need_show_update", true);
        image = fv(R.id.start_image);
        requestData();
        new Handler().postDelayed(new Runnable() {
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
        }, 2000);
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
            Picasso.with(this).load(dataObj.getString("url")).placeholder(R.mipmap.launch).into(image);
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
}
