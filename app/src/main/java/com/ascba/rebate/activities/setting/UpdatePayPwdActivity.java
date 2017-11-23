package com.ascba.rebate.activities.setting;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
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

public class UpdatePayPwdActivity extends BaseDefaultNetActivity {

    private PasswordInputView passwordInputView;
    private TextView tvForget;
    private static final int SET_PWD = 853;

    @Override
    protected int bindLayout() {
        return R.layout.activity_update_pay_pwd;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        tvForget = fv(R.id.forget_pay_pwd);
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
    }

    public void requestPWDNetwork(int what, String number) {
        AbstractRequest request = buildRequest(UrlUtils.payPassword, RequestMethod.POST, null);
        request.add("level_pwd", EncodeUtils.encryptPsd(number));
        request.add("relevel_pwd", 1);
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == SET_PWD) {
            SetPayPwdActivity.startSet(this);
            finish();
        }
    }

    public void forget(View view) {
        startActivity(ForgetPayPwdActivity.class, null);
        finish();
    }
}
