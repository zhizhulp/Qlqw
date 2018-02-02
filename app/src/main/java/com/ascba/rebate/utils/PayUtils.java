package com.ascba.rebate.utils;

import android.app.Activity;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.bean.Pay;
import com.ascba.rebate.manager.PayHandler;
import com.ascba.rebate.manager.ToastManager;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

/**
 * Created by Jero on 2017/10/17 0017.
 */

public class PayUtils {

    public static String BALANCE = "balance";
    public static String WX_PAY = "wxpay";
    public static String ALI_PAY = "alipay";

    public String type;
    public String money;
    private Activity activity;
    private PayListener listener;

    private static PayUtils payUtils;

    private PayUtils() {
    }

    public static PayUtils getInstance() {
        if (payUtils == null)
            synchronized (PayUtils.class) {
                if (payUtils == null)
                    payUtils = new PayUtils();
            }
        return payUtils;
    }

    public PayUtils register(Activity activity, PayListener payListener) {
        clear();
        payUtils.activity = activity;
        payUtils.listener = payListener;
        return payUtils;
    }

    public void clear() {
        payUtils.type = null;
        payUtils.money = null;
    }

    public void unregister() {
        payUtils.activity = null;
        payUtils.listener = null;
    }

    public void goSuccess() {
        if (listener != null)
            listener.payFinish(type);
    }

    public void requestPay(Pay data) {
        if (type.equals(ALI_PAY)) {
            requestForAli(activity, data.getAliPayInfo());
        } else if (type.equals(WX_PAY)) {
            requestForWX(activity, data.getWxPayInfo());
        } else if (type.equals(BALANCE)) {
            goSuccess();
        }
    }

    public void requestPay(String type, String money, Pay data) {
        this.type = type;
        this.money = money;
        requestPay(data);
    }

    private void requestForWX(Activity activity, Pay.WXPay wxpay) {
        if (!MyApplication.getInstance().getWXAPI().isWXAppInstalled()) {
            ToastManager.show("您没有安装微信客户端。无法使用微信支付");
        } else {
            PayReq req = new PayReq();
            req.appId = wxpay.getAppid();
            req.nonceStr = wxpay.getNoncestr();
            req.packageValue = wxpay.get_package();
            req.partnerId = wxpay.getPartnerid();
            req.prepayId = wxpay.getPrepayid();
            req.timeStamp = wxpay.getTimestamp() + "";
            req.sign = wxpay.getSign();
            IWXAPI iwxapi = WXAPIFactory.createWXAPI(activity, null);
            iwxapi.registerApp(wxpay.getAppid());
            iwxapi.sendReq(req);
        }
    }

    private void requestForAli(final Activity activity, final String payInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(payInfo, true);
                Message msg = new Message();
                msg.obj = result;
                new PayHandler(activity.getMainLooper(), activity).sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public interface PayListener {
        void payFinish(String type);
    }
}
