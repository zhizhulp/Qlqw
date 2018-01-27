package com.ascba.rebate.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.PayResult;
import com.ascba.rebate.utils.PayUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by 李平 on 2017/9/15 14:03
 * Describe: 支付宝支付回调处理
 */

public class PayHandler extends Handler {
    private Activity context;

    public PayHandler(Looper looper, Activity context) {
        super(looper);
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        @SuppressWarnings("unchecked")
        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
        //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为9000则代表支付成功
        if (TextUtils.equals(resultStatus, "9000")) {
            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
            showToast(context.getString(R.string.success_pay));
            try {
                JSONObject jObj = new JSONObject(resultInfo);
                JSONObject trObj = jObj.optJSONObject("alipay_trade_app_pay_response");
                String total_amount = trObj.optString("total_amount");
                Log.d("a_li_pay", "handleMessage: "+total_amount);
                PayUtils.getInstance().goSuccess();
            } catch (JSONException e) {
                showToast(context.getString(R.string.failed_pay));
            }
        } else if (TextUtils.equals(resultStatus, "6002")) {
            showToast(context.getString(R.string.no_network));
        } else if (TextUtils.equals(resultStatus, "6001")) {
            showToast(context.getString(R.string.cancel_pay));
        } else {
            showToast(context.getString(R.string.failed_pay));
        }
    }

    private void showToast(String str) {
        ToastManager.show(str);
//        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
    }
}
