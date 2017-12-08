package com.ascba.rebate.utils;

import android.content.Intent;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.BaseDefaultPayActivity;
import com.ascba.rebate.bean.Pay;
import com.ascba.rebate.manager.PayHandler;
import com.tencent.mm.opensdk.modelpay.PayReq;

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
    public String info;
    private Class<?> cls;
    private BaseDefaultPayActivity activity;
    private boolean paySuccess;
    private boolean isResult = false;

    private static PayUtils payUtils;

    private PayUtils() {
    }

    public static PayUtils getInstance() {
        if (payUtils == null)
            synchronized (AppConfig.class) {
                if (payUtils == null)
                    payUtils = new PayUtils();
            }
        return payUtils;
    }

    public PayUtils setInit(BaseDefaultPayActivity activity, Class<?> successCls) {
        return this.setInit(activity, successCls, false);
    }

    public PayUtils setInit(BaseDefaultPayActivity activity, Class<?> successCls, boolean result) {
        clear();
        isResult = result;
        payUtils.activity = activity;
        payUtils.cls = successCls;
        return payUtils;
    }

    public void clear() {
        payUtils.type = null;
        payUtils.money = null;
        paySuccess = false;
        isResult = false;
    }

    public void isOk() {
        paySuccess = true;
    }

    public boolean getResultOK() {
        return paySuccess;
    }

    public void goSuccess(Intent intent) {
        isOk();
        if (cls != null) {
            intent.setClass(activity, cls);
            if (isResult)
                activity.startActivityForResult(intent, CodeUtils.REQUEST_PAY);
            else
                activity.startActivity(intent);
        } else
            activity.payResult(type);
    }

    public void requestPay(Pay data) {
        PayUtils.getInstance().info = data.getSuccess_info();
        if (type.equals(ALI_PAY)) {
            requestForAli(activity, data.getAliPayInfo());
        } else if (type.equals(WX_PAY)) {
            requestForWX(activity, data.getWxPayInfo());
        } else if (type.equals(BALANCE)) {
            Intent intent = new Intent();
            intent.putExtra("type", "余额支付");
            intent.putExtra("money", PayUtils.getInstance().money);
            goSuccess(intent);
        }
    }

    public void requestPay(String type, String money, Pay data) {
        this.type = type;
        this.money = money;
        requestPay(data);
    }

    private void requestForWX(BaseDefaultNetActivity activity, Pay.WXPay wxpay) {
        if (!MyApplication.getInstance().getWXAPI().isWXAppInstalled()) {
            activity.showToast("您没有安装微信客户端。无法使用微信支付");
        } else {
            PayReq req = new PayReq();
            req.appId = wxpay.getAppid();
            req.nonceStr = wxpay.getNoncestr();
            req.packageValue = wxpay.get_package();
            req.partnerId = wxpay.getPartnerid();
            req.prepayId = wxpay.getPrepayid();
            req.timeStamp = wxpay.getTimestamp() + "";
            req.sign = wxpay.getSign();
            MyApplication.getInstance().getWXAPI().sendReq(req);
        }
    }

    private void requestForAli(final BaseDefaultNetActivity activity, final String payInfo) {
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
}
