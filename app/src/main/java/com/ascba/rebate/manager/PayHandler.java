package com.ascba.rebate.manager;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
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
    private BaseDefaultNetActivity context;

    public PayHandler(Looper looper, BaseDefaultNetActivity context) {
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
            context.showToast(context.getString(R.string.success_pay));
            try {
                JSONObject jObj = new JSONObject(resultInfo);
                JSONObject trObj = jObj.optJSONObject("alipay_trade_app_pay_response");
                String total_amount = trObj.optString("total_amount");
                Intent intent = new Intent();
                intent.putExtra("money", total_amount+"元");
                intent.putExtra("type", "支付宝支付");
                PayUtils.getInstance().goSuccess(intent);
            } catch (JSONException e) {
                context.showToast(context.getString(R.string.failed_pay));
            }
        } else if (TextUtils.equals(resultStatus, "6002")) {
            context.showToast(context.getString(R.string.no_network));
        } else if (TextUtils.equals(resultStatus, "6001")) {
            context.showToast(context.getString(R.string.cancel_pay));
        } else {
            context.showToast(context.getString(R.string.failed_pay));
        }
    }
}
