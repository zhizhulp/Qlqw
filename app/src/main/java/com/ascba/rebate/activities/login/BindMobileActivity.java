package com.ascba.rebate.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.LoginNextEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.ActivityManager;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.RegexUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.WXUtils;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by Jero on 2017/9/16 0016.
 */

public class BindMobileActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private EditText etCode, etMobile;
    private TextView tvTime;
    private Button btnOK;
    private String mobile, code;
    private boolean codePressed = false;
    private int codeLength;
    private CheckBox cb;
    private View tvProtocol;

    @Override
    protected int bindLayout() {
        return R.layout.activity_wx_bind;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickBack(View back) {
                dm.showAlertDialog2("首次微信登录需要绑定手机账号，是否取消微信登录？", null, null, new DialogManager.Callback() {
                    @Override
                    public void handleLeft() {
                        finish();
                    }
                });
            }
        });
        etMobile = fv(R.id.bind_mobile_et);
        etCode = fv(R.id.bind_code_et);
        tvTime = fv(R.id.bind_time_tv);
        btnOK = fv(R.id.bind_btn);
        cb = fv(R.id.cb);
        tvProtocol = fv(R.id.tv_protocol);
        tvProtocol.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        code = etCode.getText().toString();
        setOKBtnEnable(etCode);
    }

    private void setOKBtnEnable(EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                codeLength = etCode.getText().toString().length();
                if (codeLength > 0) {
                    btnOK.setEnabled(true);
                } else {
                    btnOK.setEnabled(false);
                }
            }
        });
    }

    private void identityMobile() {
        mobile = etMobile.getText().toString();
        if (mobile.length() != 0) {//没输入的情况下
            if (RegexUtils.isMobilePhoneNum(mobile)) {
                requestCodeNetwork(0);
            } else {
                showToast("输入不符合规范，请重新输入");
            }
        } else {
            showToast("请输入11位手机号码");
        }
    }

    public void TimeCount() {
        new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tvTime.setTextColor(getResources().getColor(R.color.grey_tv));
                tvTime.setText("" + l / 1000 + "s后重发");
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_btn:
                if(cb.isChecked()){
                    if (codePressed) {
                        if (codeLength == 6) {
                            requestVerifyNetwork(1);
                        } else {
                            showToast("请输入六位验证码");
                        }
                    } else {
                        showToast("请先获取验证码");
                    }
                }else {
                    showToast("请先同意《钱来钱往服务协议》");
                }
                break;
            case R.id.bind_time_tv:
                codePressed = true;
                identityMobile();
                break;
            case R.id.tv_protocol:
                WebViewBaseActivity.start(this,"服务协议",UrlUtils.agreementRegister);
                break;
        }
    }

    public void requestCodeNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.WechatLoginGetVerify, RequestMethod.GET, null);
        request.add("mobile", mobile);
        executeNetwork(what, "请稍后", request);
    }

    public void requestVerifyNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.WechatLoginBind, RequestMethod.POST, LoginNextEntity.class);
        code = etCode.getText().toString();
        request.add("mobile", mobile);
        request.add("unionid", WXUtils.getInstance().unionId);
        request.add("verify", code);
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        switch (what) {
            case 0:
                showToast(result.getMsg());
                etCode.requestFocus();
                TimeCount();
                break;
            case 1:
                showToast("绑定成功");
                LoginNextEntity loginNextEntity = (LoginNextEntity) result.getData();
                Intent intent = new Intent();
                intent.putExtra(WXUtils.INFO, JSON.toJSONString(loginNextEntity));
                setResult(RESULT_OK, intent);
                finish();
                ActivityManager.getInstance().removeActivity(this);
                break;
        }
    }
}
