package com.ascba.rebate.base.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.utils.EncodeUtils;
import com.ascba.rebate.utils.PackageUtils;
import com.ascba.rebate.view.MoneyBar;
import com.yanzhenjie.nohttp.tools.NetUtils;

import java.util.HashMap;
import java.util.Map;

public class WebViewBaseActivity extends AppCompatActivity {
    private String url;
    private WebView webView;
    private MoneyBar mb;
    private String name;

    public static void start(Context context, String name, String url) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(url)) {
            Log.e("WebViewBaseActivity", "start: name为空或者url为空");
            return;
        }
        Intent intent = new Intent(context, WebViewBaseActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_base);
        initViews();
        getMsgFromBefore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getMsgFromBefore() {
        Intent intent = getIntent();
        if (intent != null) {
            boolean netAva = NetUtils.isNetworkAvailable();
            if (!netAva) {
                return;
            }
            name = intent.getStringExtra("name");
            url = intent.getStringExtra("url");
            mb.setTextTitle(name);
            if (url != null) {
                final ProgressDialog p = new ProgressDialog(this, R.style.dialog);
                p.setMessage("请稍后");
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        p.dismiss();
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);

                    }
                });
                webView.loadUrl(url, createHeader());
                p.show();
            }
        }
    }

    private Map<String, String> createHeader() {
        Map<String, String> map = new HashMap<>();
        map.put("clientfrom", "android" + PackageUtils.getAndroidVersion());
        map.put("deviceuuid", PackageUtils.getDeviceId());
        String nonceStr = EncodeUtils.makeNonceStr();
        map.put("noncestr", nonceStr);
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", EncodeUtils.makeSign(nonceStr, url));
        map.put("sessionId", AppConfig.getInstance().getString("session_id", null));
        map.put("accessToken", AppConfig.getInstance().getString("access_token", null));
        return map;
    }

    private void initViews() {
        webView = ((WebView) findViewById(R.id.webView));
        mb = ((MoneyBar) findViewById(R.id.mb_protocol));
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack();  //返回上一页
        }
    }

    /**
     * 拦截实体键(MENU  BACK  POWER  VOLUME  HOME)的点击事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {  //按下实体的返回键
            if (webView.canGoBack()) {  //说明WebView有上一页
                webView.goBack();  //WebView返回上一页
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
