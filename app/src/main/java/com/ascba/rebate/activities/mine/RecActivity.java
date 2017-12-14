package com.ascba.rebate.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by 李平 on 2017/12/12 15:12
 * Describe: 推荐二维码
 */

public class RecActivity extends BaseDefaultNetActivity {

    private ImageView imCode;
    private ImageView imHead;
    private TextView tvName;
    private TextView tvClass;

    @Override
    protected int bindLayout() {
        return R.layout.activity_rec;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        imCode = fv(R.id.im_code);

        imHead = fv(R.id.im_head);
        tvName = fv(R.id.tv_name);
        tvClass = fv(R.id.tv_class);

        requestData();
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.getMemberQrcode, RequestMethod.GET, null);
        executeNetwork(0,"请稍后",request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        String data = (String) result.getData();
        JSONObject dataObj = JSON.parseObject(data);
        Picasso.with(this).load(dataObj.getString("qrcode")).into(imCode);
        Picasso.with(this).load(dataObj.getString("avatar")).placeholder(R.mipmap.head_loading).into(imHead);
        tvName.setText(dataObj.getString("nickname"));
        tvClass.setText(dataObj.getString("rule"));
    }

    @Override
    protected void mHandle404(int what, Result result) {
        super.mHandle404(what, result);
        finish();
    }

    @Override
    protected void mHandleReLogin(int what, Result result) {
        super.mHandleReLogin(what, result);
    }

    @Override
    protected void mHandleFailed(int what) {
        super.mHandleFailed(what);
        finish();
    }

    @Override
    protected void mHandleNoNetwork(int what) {
        super.mHandleNoNetwork(what);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        requestData();
    }
}
