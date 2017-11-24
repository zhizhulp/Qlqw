package com.ascba.rebate.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.BuildConfig;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.main.MainActivity;
import com.ascba.rebate.activities.register.RegisterActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.LoginEntity;
import com.ascba.rebate.bean.LoginNextEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.ActivityManager;
import com.ascba.rebate.manager.JpushSetManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.Demo;
import com.ascba.rebate.utils.PackageUtils;
import com.ascba.rebate.utils.RegexUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.WXUtils;
import com.ascba.rebate.view.picasso.CropCircleTransformation;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.LinkedHashSet;
import java.util.Set;

public class LoginActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private static final int WX_OPEN_ID = 162;
    private static final int WX_LOGIN = 422;
    private static final int WX_BIND = 564;
    private static final int WX_GET_INFO = 167;

    private EditText etNumber, etCode;
    private TextView tvTime;
    private ImageView ivAvatar;
    private Button btnLogin;
    private String mobile, code;
    private boolean codePressed = false;
    private int codeLength;

    @Override
    protected int setUIMode() {
        return UIMODE_TRANSPARENT_FULLSCREEN;
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        if (BuildConfig.DEBUG) {
            final Demo demo = new Demo();
            fv(R.id.select).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    demo.showTest1(LoginActivity.this);
                }
            });
        }
        if (AppConfig.getInstance().getBoolean("first_login", true)) {
            etNumber = fv(R.id.login_et_number);
            etNumber.setText(AppConfig.getInstance().getString("mobile", null));
            etNumber.requestFocus(etNumber.getText().length());
            etCode = fv(R.id.login_et_code);
            tvTime = fv(R.id.login_time_tv);
            ivAvatar = fv(R.id.login_iv_avatar);
            TextView tvLogin = fv(R.id.register_tv);
            btnLogin = fv(R.id.login_btn);
            btnLogin.setOnClickListener(this);
            tvLogin.setOnClickListener(this);
            tvTime.setOnClickListener(this);
            code = etCode.getText().toString();
            setLoginBtnEnable(etCode);
            setHeadIcon(AppConfig.getInstance().getString("avatar", null));
            fv(R.id.im_wx).setOnClickListener(this);
        } else {
            startActivity(MainActivity.class, null);
            finish();
        }
    }

    public void requestCodeNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.getLoginVerify, RequestMethod.POST, LoginEntity.class);
        request.add("mobile", mobile);
        executeNetwork(what, "请稍后", request);
    }

    public void requestLoginNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.login, RequestMethod.POST, LoginNextEntity.class);
        mobile = etNumber.getText().toString();
        code = etCode.getText().toString();
        request.add("mobile", mobile);
        request.add("verify", code);
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        switch (what) {
            case 0:
                showToast(result.getMsg());
                etNumber.clearFocus();
                etCode.requestFocus();
                TimeCount();
                LoginEntity loginEntity = (LoginEntity) result.getData();
                String avatar = AppConfig.getInstance().getString("avatar", null);
                if (avatar == null) {
                    setHeadIcon(loginEntity.getAvatar());
                } else {
                    if (!avatar.equals(loginEntity.getAvatar())) {
                        setHeadIcon(loginEntity.getAvatar());
                    }
                }
                break;
            case 1:
                saveLoginInfo((LoginNextEntity) result.getData());
                finish();
                break;
            case WX_LOGIN:
                LoginNextEntity loginNextEntity = (LoginNextEntity) result.getData();
                if (loginNextEntity.getStatus() == 1) {
                    startActivityForResult(BindMobileActivity.class, null, WX_BIND);
                } else if (loginNextEntity.getStatus() == 2) {
                    saveLoginInfo(loginNextEntity);
                    showToast("微信登录成功");
                    finish();
                } else {
                    showToast("微信授权失败，请使用其他方式登录。");
                }
                break;
            case WX_GET_INFO:
                JSONObject jsonObject = JSON.parseObject(result.getData().toString());
                String secret = jsonObject.getString("appsecret");
                if (secret != null && !secret.isEmpty()) {
                    CodeUtils.WX_APP_SECRET = secret;
                    WXUtils.getInstance().wChatLogin(this);
                } else {
                    showToast("微信授权失败，请使用其他方式登录。");
                }
                break;
        }
    }

    private void saveLoginInfo(LoginNextEntity loginInfo) {
        LoginNextEntity loginNextEntity = loginInfo;
        LoginNextEntity.UserInfoBean user_info = loginNextEntity.getUser_info();
        LoginNextEntity.UpdateTokenBean updateToken = loginNextEntity.getUpdate_token();
        AppConfig appConfig = AppConfig.getInstance();
        String mobileOld = appConfig.getString("mobile", null);
        if (mobileOld == null) {//第一次登陆
            startActivity(MainActivity.class, null);
        } else {
            if (user_info.getMobile().equals(mobileOld)) {//手机号相同
                if (ActivityManager.getInstance().getSize() > 1) {//当前还有其他activity
                    setResult(RESULT_OK, getIntent());
                } else {
                    startActivity(MainActivity.class, null);
                }

            } else {//手机号不同
                appConfig.clearXml();
                appConfig.putBoolean("need_guide", false);
                ActivityManager.getInstance().removeAllExceptOne(this);
                startActivity(MainActivity.class, null);
            }
        }
        appConfig.putString("session_id", updateToken.getSession_id());
        String accessToken = updateToken.getAccess_token();
        setJPushAlias(accessToken);
        appConfig.putString("access_token", accessToken);
        appConfig.putBoolean("first_login", false);//add 2017/8/24 14:41
        appConfig.putString("avatar", user_info.getAvatar());
        appConfig.putString("mobile", user_info.getMobile());
        appConfig.putString("nickname", user_info.getNickname());
        appConfig.putString("sex", user_info.getSex());
        appConfig.putInt("age", user_info.getAge());
        appConfig.putString("location", user_info.getLocation());
        appConfig.putInt("card_status", user_info.getCard_status());
        appConfig.putInt("company_status", user_info.getCompany_status());
        appConfig.putString("group_name", user_info.getGroup_name());
        appConfig.putInt("is_level_pwd", user_info.getIs_level_pwd());
        appConfig.putString("realname", user_info.getRealname());
    }

    private void setHeadIcon(String avatar) {
        Picasso.with(getApplicationContext()).load(avatar)
                .transform(new CropCircleTransformation())
                .placeholder(R.mipmap.login_avatar).into(ivAvatar);
    }

    private void setJPushAlias(String accessToken) {
        if (!accessToken.equals(AppConfig.getInstance().getString("access_token", null))) {
            JpushSetManager jpush = new JpushSetManager(this, 0);
            jpush.setAlias(accessToken);
            jpush.setTag(getTag(PackageUtils.isAppDebug(this)));
        }
    }

    //调用JPush API设置Tag
    private Set<String> getTag(boolean appDebug) {
        Set<String> tagSet = new LinkedHashSet<String>();
        if (appDebug) {
            tagSet.add("debug");
        } else {
            tagSet.add("release");
        }
        return tagSet;
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
            case R.id.login_time_tv://获取验证码按钮
                codePressed = true;
                identityMobile();
                break;
            case R.id.login_btn://登录按钮
                if (codePressed) {
                    if (codeLength == 6) {
                        requestLoginNetwork(1);
                    } else {
                        showToast("请输入六位验证码");
                    }
                } else {
                    showToast("请先获取验证码");
                }
                break;
            case R.id.register_tv://注册
                startActivityForResult(RegisterActivity.class, null, CodeUtils.REQUEST_REGISTER_PHONE);
                break;
            case R.id.im_wx://微信登录
                if (CodeUtils.WX_APP_SECRET.isEmpty())
                    requestGetWChatInfo();
                else
                    WXUtils.getInstance().wChatLogin(this);
                break;
        }
    }

    private void identityMobile() {
        mobile = etNumber.getText().toString();
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

    private void setLoginBtnEnable(EditText et) {
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
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (WXUtils.getInstance().loginCode == 0) {
            requestWChatOpen();
            WXUtils.getInstance().loginCode = 1;
        }
    }

    public void requestWChatOpen() {
        WXUtils.getInstance().requestWChatOpen(WX_OPEN_ID, new WXUtils.WXHttpResponseListener(this, "请稍后") {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if (what == WX_OPEN_ID) {
                    JSONObject jsonObject = JSON.parseObject(response.get());
                    WXUtils.getInstance().openId = jsonObject.getString("openid");
                    WXUtils.getInstance().unionId = jsonObject.getString("unionid");
                    if (WXUtils.getInstance().unionId != null && !WXUtils.getInstance().unionId.isEmpty())
                        requestWChatLogin();
                    else
                        showToast("微信授权失败，请使用其他方式登录。");
                }
            }
        });
    }

    public void requestGetWChatInfo() {
        AbstractRequest request = buildRequest(UrlUtils.getWechatAppInfo, RequestMethod.GET, null);
        executeNetwork(WX_GET_INFO, "请稍后", request);
    }

    public void requestWChatLogin() {
        AbstractRequest request = buildRequest(UrlUtils.Wechatlogin, RequestMethod.POST, LoginNextEntity.class);
        request.add("unionid", WXUtils.getInstance().unionId);
        executeNetwork(WX_LOGIN, "请稍后", request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WX_BIND && resultCode == RESULT_OK) {
            showToast("微信登录成功");
            saveLoginInfo(JSON.parseObject(data.getStringExtra(WXUtils.INFO), LoginNextEntity.class));
            finish();
        }
    }
}
