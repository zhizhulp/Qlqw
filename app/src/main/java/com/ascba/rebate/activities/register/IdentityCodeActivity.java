package com.ascba.rebate.activities.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.ascba.rebate.view.RectangleBoxEditText;
import com.yanzhenjie.nohttp.RequestMethod;

public class IdentityCodeActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private RectangleBoxEditText etCode;
    private TextView tvNoreceive, tvTime;
    private String mobile;
    private int intCode;
    private TextView tvMobile;
    private MoneyBar moneyBar;

    @Override
    protected int bindLayout() {
        return R.layout.activity_identity_code;
    }

    @Override
    protected int setUIMode() {
        return BaseUIActivity.UIMODE_TRANSPARENT_FULLSCREEN;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        etCode = fv(R.id.et_rectangle);
        tvNoreceive = fv(R.id.identity_noreceive);
        tvTime = fv(R.id.identity_time_tv);
        tvMobile = fv(R.id.id_mobile_tv);
        moneyBar = fv(R.id.mb);
        tvNoreceive.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mobile = bundle.getString("mobile");
        tvMobile.setText("我们已向" + mobile + "发送验证码短信");
        moneyBar.setCallBack(moneyBar.new CallbackImp() {
            @Override
            public void clickBack(View back) {//dialog
                setBackDialog();

            }
        });
        timeCount();
        initEvent();
    }

    private void setBackDialog() {
        dm.showAlertDialog2("短信有延迟，请稍等...", "返回", "再等一下", new DialogManager.Callback() {
            @Override
            public void handleLeft() {
                finish();
            }
        });
    }

    public void requestIdentityCodeNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.checkRegisterVerify, RequestMethod.GET, null);
        request.add("mobile", mobile);
        request.add("verify", intCode);
        executeNetwork(what, "请稍后", request);
    }

    public void requestCodeNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.getRegisterVerify, RequestMethod.GET, null);
        request.add("mobile", mobile);
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            Bundle bundle = new Bundle();
            bundle.putString("mobile", mobile);
            bundle.putInt("code", intCode);
            startActivityForResult(RegisterNextActivity.class, bundle, CodeUtils.REQUEST_REGISTER_DATA);
        } else if (what == 1) {
            showToast(result.getMsg());
            timeCount();
        }
    }

    @Override
    protected void mHandle404(int what, Result result) {
        super.mHandle404(what, result);
        if (what == 0) {
            etCode.setText(null);
        }
    }

    private void initEvent() {
        //当密码输入完成时的回调
        etCode.setOnInputFinishedListener(new RectangleBoxEditText.OnInputFinishedListener() {
            @Override
            public void onInputFinished(String password) {
                //输入完成，验证码校验接口
                intCode = Integer.parseInt(password);
                requestIdentityCodeNetwork(0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.identity_noreceive:
                showToast("收不到");
                break;
            case R.id.identity_time_tv:
                requestCodeNetwork(1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.REQUEST_REGISTER_DATA) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, new Intent(this, RegisterActivity.class));
                finish();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }

        }
    }

    public void timeCount() {
        new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tvTime.setTextColor(getResources().getColor(R.color.grey_tv));
                tvTime.setText("" + l / 1000 + " s后重发");
                tvTime.setEnabled(false);
            }

            @Override
            public void onFinish() {
                tvTime.setTextColor(getResources().getColor(R.color.grey_black_tv));
                tvTime.setText("重新发送");
                tvTime.setEnabled(true);
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setBackDialog();
            return true;
        }
        return false;
    }
}


