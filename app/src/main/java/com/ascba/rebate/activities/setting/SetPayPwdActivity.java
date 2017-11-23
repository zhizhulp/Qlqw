package com.ascba.rebate.activities.setting;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.EncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.keyboard.NumKeyboardUtil;
import com.ascba.rebate.view.keyboard.PasswordInputView;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by Jero on 2017/9/16 0016.
 */

public class SetPayPwdActivity extends BaseDefaultNetActivity {

    private PasswordInputView passwordInputView;
    private static final int SET_PWD = 853;

    public static final String IS_SET = "is_set";

    public static void startSet(Context context) {
        Intent intent = new Intent(context, SetPayPwdActivity.class);
        intent.putExtra(IS_SET, true);
        context.startActivity(intent);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_set_pay_pwd;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        passwordInputView = fv(R.id.password_input);
        passwordInputView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        NumKeyboardUtil numKeyboardUtil = new NumKeyboardUtil((KeyboardView) fv(R.id.keyboard_view), this, passwordInputView);
        passwordInputView.setWatcher(new PasswordInputView.TextWatcher() {
            @Override
            public void complete(String number) {
                requestPWDNetwork(SET_PWD, number);
            }
        });
        if (getIntent().getBooleanExtra(IS_SET, false)) {
            ((TextView) fv(R.id.psd_title_tv)).setText("请输入新支付密码");
        }
    }

    public void requestPWDNetwork(int what, String number) {
        AbstractRequest request = buildRequest(UrlUtils.payPassword, RequestMethod.POST, null);
        request.add("level_pwd", EncodeUtils.encryptPsd(number));
        request.add("relevel_pwd", 0);
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == SET_PWD) {
            showToast(result.getMsg());
            AppConfig appConfig = AppConfig.getInstance();
            if (appConfig.getInt("is_level_pwd", 0) == 0) {
                appConfig.putInt("is_level_pwd", 1);
                setResult(RESULT_OK);
            }
            finish();
        }
    }
}
