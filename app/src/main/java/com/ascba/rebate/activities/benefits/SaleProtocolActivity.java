package com.ascba.rebate.activities.benefits;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.EncodeUtils;
import com.ascba.rebate.utils.PackageUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 李平 on 2017/9/23 14:06
 * Describe: 开通寄卖协议
 */

public class SaleProtocolActivity extends BaseDefaultNetActivity {
    private String url;

    @Override
    protected int bindLayout() {
        return R.layout.activity_sale_protocol;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface"})
    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        getParams();
        WebView webView = fv(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(this, "nice");
        webView.loadUrl(url, createHeader());
    }

    private void getParams() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
    }

    public static void jumpIntent(Activity context, String url) {
        Intent intent = new Intent(context, SaleProtocolActivity.class);
        intent.putExtra("url", url);
        context.startActivityForResult(intent, CodeUtils.REQUEST_OPEN);
    }

    private Map<String, String> createHeader() {
        Map<String, String> map = new HashMap<>();
        map.put("clientfrom", "android" + PackageUtils.getAndroidVersion());
        map.put("deviceuuid", PackageUtils.getDeviceId());
        String nonceStr = EncodeUtils.makeNonceStr();
        map.put("noncestr", nonceStr);
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", EncodeUtils.makeSign(nonceStr, UrlUtils.protocol));
        map.put("sessionId", AppConfig.getInstance().getString("session_id", null));
        map.put("accessToken", AppConfig.getInstance().getString("access_token", null));
        return map;
    }

    @JavascriptInterface
    public void requestOpen() {
        AbstractRequest request = buildRequest(UrlUtils.openProtocol, RequestMethod.GET, null);
        executeNetwork(1, request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        showToast(result.getMsg());
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
