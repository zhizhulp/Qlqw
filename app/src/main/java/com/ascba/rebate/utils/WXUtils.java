package com.ascba.rebate.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.manager.ToastManager;
import com.ascba.rebate.net.CallServer;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.RestRequest;

/**
 * Created by 李平 on 2017/10/14 10:34
 * Describe:
 */

public class WXUtils {

    public int loginCode = 1;
    public String wxCode;
    public String openId;
    public String unionId;
    private static WXUtils wxUtils;
    public static String INFO = "info";

    private WXUtils() {
    }

    public static WXUtils getInstance() {
        if (wxUtils == null)
            synchronized (AppConfig.class) {
                if (wxUtils == null)
                    wxUtils = new WXUtils();
            }
        return wxUtils;
    }

    public void wChatLogin(Context context) {
        if (!MyApplication.getInstance().getWXAPI().isWXAppInstalled()) {
            ToastManager.show("您没有安装微信客户端，请下载并安装微信后使用。");
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "qlqw";
        MyApplication.getInstance().getWXAPI().sendReq(req);
    }

    public void setCode(SendAuth.Resp resp) {
        if ("qlqw".equals(resp.state))
            wxCode = resp.code;
        else {
            loginCode = 1;
            Log.e("wx", "setCode: state出问题了啊！");
        }
    }

    public void requestWChatOpen(int what, WXHttpResponseListener listener) {
        Request<String> request = NoHttp.createStringRequest("https://api.weixin.qq.com/sns/oauth2/access_token");
        request.add("appid", CodeUtils.WX_APP_ID);
        request.add("secret", CodeUtils.WX_APP_SECRET);
        request.add("code", WXUtils.getInstance().wxCode);
        request.add("grant_type", "authorization_code");
        CallServer.getInstance().addRequest(what, (RestRequest<String>) request, listener);
    }

    public abstract static class WXHttpResponseListener implements OnResponseListener<String> {
        private ProgressDialog dialog;
        private BaseUIActivity context;

        public WXHttpResponseListener(BaseUIActivity context, String message) {
            if (message != null) {
                this.dialog = new ProgressDialog(context, R.style.dialog);
                dialog.setCanceledOnTouchOutside(false);//点击外部不可取消
                dialog.setCancelable(true);//返还键可取消
                dialog.setMessage(message);
            }
            this.context = context;
        }

        @Override
        public void onStart(int what) {
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        }

        @Override
        public abstract void onSucceed(int what, Response<String> response);

        @Override
        public void onFailed(int what, Response<String> response) {
            context.showToast("网络错误，微信登录失败，请稍后再试。");
            Log.e("wx", "微信接口获取失败", response.getException());
        }

        @Override
        public void onFinish(int what) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
