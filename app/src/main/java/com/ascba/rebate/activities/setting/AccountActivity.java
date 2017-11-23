package com.ascba.rebate.activities.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.utils.CodeUtils;

/**
 * Created by Jero on 2017/9/16 0016.
 */

public class AccountActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private static final int SET_FPR = 914;
    private AppConfig appConfig;

    private RelativeLayout rlPaymentPwd, rlFingerprint, rlWChat, rlQQ, rlWeiBo, rlTaoBao;
    private TextView tvMobile, tvWChat, tvQQ, tvWeiBo, tvTaoBao, tvPayPwd, tvPayType, tvFingerprint;

    @Override
    protected int bindLayout() {
        return R.layout.activity_account;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        tvMobile = fv(R.id.account_mobile_tv);
        tvPayPwd = fv(R.id.account_payment_tv);
        tvPayType = fv(R.id.account_payment_type_tv);
        tvFingerprint = fv(R.id.account_fingerprint_tv);
        appConfig = AppConfig.getInstance();
        tvMobile.setText(appConfig.getString("mobile", ""));
        tvPayPwd.setText(appConfig.getInt("is_level_pwd", 0) == 0 ? "设置支付密码" : "修改支付密码");
        tvPayType.setText(appConfig.getInt("is_level_pwd", 0) == 0 ? "未设置" : "已设置");
        tvFingerprint.setText(appConfig.getInt("is_fpr", 0) == 0 ? "未设置" : "已设置");
        rlPaymentPwd = fv(R.id.payment_password_rl);
        rlFingerprint = fv(R.id.fingerprint_rl);
        rlPaymentPwd.setOnClickListener(this);
        rlFingerprint.setOnClickListener(this);
        tvWChat = fv(R.id.account_wChat_tv);
        checkBindItem(tvWChat, appConfig.getInt("is_weixin", 0) == 1);
//        fv(R.id.wchat_rl).setOnClickListener(this);
    }

    private void checkBindItem(TextView view, boolean isBind) {
        view.setText(isBind ? "已绑定" : "未绑定");
        view.setTextColor(isBind ? getResources().getColor(R.color.grey_black_tv2) : getResources().getColor(R.color.blue_btn));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payment_password_rl:
                if (appConfig.getInt("is_level_pwd", 0) == 0)
                    startActivityForResult(SetPayPwdActivity.class, null, CodeUtils.REQUEST_ADD_PWD);
                else
                    startActivity(UpdatePayPwdActivity.class, null);
                break;
            case R.id.fingerprint_rl:
                startActivityForResult(FingerprintActivity.class, null, SET_FPR);
                break;
            case R.id.wchat_rl:
                if (appConfig.getInt("is_weixin", 0) == 1) {
                    dm.showUnbind("微信", new DialogManager.Callback() {
                        @Override
                        public void handleRight() {
                            //do something 解绑
                            tvWChat.setText("未绑定");
                            tvWChat.setTextColor(getResources().getColor(R.color.blue_btn));
                        }
                    });
                } else {
                    //do something 绑定
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CodeUtils.REQUEST_ADD_PWD:
                    tvPayPwd.setText(appConfig.getInt("is_level_pwd", 0) == 0 ? "设置支付密码" : "修改支付密码");
                    tvPayType.setText(appConfig.getInt("is_level_pwd", 0) == 0 ? "未设置" : "已设置");
                    break;
                case SET_FPR:
                    tvFingerprint.setText(appConfig.getInt("is_fpr", 0) == 0 ? "未设置" : "已设置");
                    break;
            }
        }
    }
}
