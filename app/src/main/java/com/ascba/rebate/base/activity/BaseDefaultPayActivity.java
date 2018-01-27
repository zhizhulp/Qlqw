package com.ascba.rebate.base.activity;

import android.os.Bundle;
import android.util.Log;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.setting.SetPayPwdActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.bean.Pay;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.EncodeUtils;
import com.ascba.rebate.utils.PayUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.PaySelectDialog;
import com.ascba.rebate.view.PsdDialog;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by Jero on 2017/10/17 0017.
 */

public class BaseDefaultPayActivity extends BaseDefaultNetActivity implements PaySelectDialog.Callback, PayUtils.PayListener {
    private static final int CHECK_PWD = 300;
    private static final int PAY = 177;

    protected PaySelectDialog mPayDialog;
    protected PsdDialog mPsdDialog;
    protected PayUtils payUtils;

    protected Pay pay;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        payUtils = PayUtils.getInstance().register(this, this);
    }

    /**
     * 支付的最初入口
     */
    protected void payStart(String price, String balance) {
        if (mPayDialog == null) {
            mPayDialog = new PaySelectDialog(this, price, balance);
            mPayDialog.setCallback(this);
        } else
            mPayDialog.setBalance(price, balance);
        payUtils.money = price;
        payUtils.type = PayUtils.WX_PAY;
        mPayDialog.show();
    }

    /**
     * 第二步：验证支付密码流程（开始）
     */
    @Override
    public void goPay(String type) {
//        if (type.equals(PayUtils.WX_PAY) && !MyApplication.getInstance().getWXAPI().isWXAppInstalled()) {
//            showToast("您没有安装微信客户端。无法使用微信支付");
//            return;
//        }
        payUtils.type = type;
        if (AppConfig.getInstance().getInt("is_level_pwd", 0) == 0)//未设置支付密码
            onNoPWD(type);
        else //设置了支付密码
            onHavePWD(type);
    }

    protected void onNoPWD(String type) {
        if (type.equals(PayUtils.BALANCE)) {
            dm.showAlertDialog2(getString(R.string.no_pay_psd), null, null, new DialogManager.Callback() {
                @Override
                public void handleLeft() {
                    startActivity(SetPayPwdActivity.class, null);
                }
            });
        } else
            requestPayInfo(payUtils.type, payUtils.money, PAY);
    }

    protected void onHavePWD(String type) {
        showPsdDialog();
    }

    @Override
    public void close() {
        dm.showAlertDialog2("确定放弃本次支付？", null, null, new DialogManager.Callback() {
            @Override
            public void handleLeft() {
                mPayDialog.dismiss();
            }
        });
    }

    private void showPsdDialog() {
        if (mPsdDialog == null) {
            mPsdDialog = new PsdDialog(this, R.style.dialog, new PsdDialog.OnPasswordInput() {
                @Override
                public void inputFinish(String number) {
                    requestCheckPayParams(EncodeUtils.getPayPsd(number), CHECK_PWD);
                }

                @Override
                public void inputCancel() {
                    mPsdDialog.dismiss();
                }
            });
        }
        mPsdDialog.showMyDialog();
    }

    protected void requestCheckPayParams(String payPsd, int what) {
        AbstractRequest request = buildRequest(UrlUtils.checkPayPassword, RequestMethod.POST, null);
        request.add("level_pwd", payPsd);
        executeNetwork(what, request);
    }

    /**
     * 第三步子：去往获取订单详情（支付密码验证成功也走这步）
     */
    protected void requestPayInfo(String type, String money, int what) {
        Log.e(TAG, "requestPayInfo: 请重写requestPayInfo方法，执行获取订单信息的方法");
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == CHECK_PWD) {
            requestPayInfo(payUtils.type, payUtils.money, PAY);
        } else if (what == PAY) {
            onPay(result);
        }
    }

    /**
     * 第四步：根据分类去往支付
     */
    protected void onPay(Result result) {
        if (mPayDialog != null && mPayDialog.isShowing()) {
            mPayDialog.dismiss();
        }
        if (mPsdDialog != null && mPsdDialog.isShowing()) {
            mPsdDialog.dismiss();
        }
        pay = (Pay) result.getData();
        payUtils.requestPay(pay);
    }

    /**
     * 第五步：支付完成回调
     */
    @Override
    public void payFinish(String type) {
        payUtils.clear();
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        payUtils.clear();
        payUtils.unregister();
    }
}
