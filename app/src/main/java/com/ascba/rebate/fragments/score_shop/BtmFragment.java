package com.ascba.rebate.fragments.score_shop;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ascba.rebate.R;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.view.vertical_drag.CustWebView;

/**
 * 礼品商品详情下半个
 */
public class BtmFragment extends BaseDefaultNetFragment {

    private boolean hasInited = false;
    private CustWebView webView;

    public BtmFragment() {
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_btm;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initViews() {
        super.initViews();
        webView = fv(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }

    public void initView(String url) {
        if (null != webView && !hasInited && url != null) {
            hasInited = true;
            webView.loadUrl(url);
        }
    }


}
