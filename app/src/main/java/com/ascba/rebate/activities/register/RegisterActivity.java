package com.ascba.rebate.activities.register;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.RegexUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

public class RegisterActivity extends BaseDefaultNetActivity implements View.OnClickListener, TextWatcher {
    private EditText etMobile;
    private Button btnRegister;
    private TextView tvProtocol;
    private String mobile;
    private ImageView ivClear;

    @Override
    protected int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        etMobile = fv(R.id.register_et_number);
        btnRegister = fv(R.id.register_btn);
        tvProtocol = fv(R.id.register_protocol);
        ivClear=fv(R.id.register_clear);
        ivClear.setOnClickListener(this);
        etMobile.addTextChangedListener(this);
        btnRegister.setOnClickListener(this);
        tvProtocol.setOnClickListener(this);
    }

    @Override
    protected int setUIMode() {
        return BaseUIActivity.UIMODE_TRANSPARENT_FULLSCREEN;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn:
                mobile = etMobile.getText().toString();
                if (RegexUtils.isMobilePhoneNum(mobile)) {//验证电话号码
                    requestCodeNetwork(0);
                } else {
                    showToast("请输入11位手机号码");
                }
                break;
            case R.id.register_protocol:
                Bundle bundle=new Bundle();
                bundle.putString("name","注册协议");
                bundle.putString("url",UrlUtils.registerProtocol);
                startActivity(WebViewBaseActivity.class,bundle);
                break;
            case R.id.register_clear:
                etMobile.getText().clear();
                break;
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        Bundle bundle = new Bundle();
        bundle.putString("mobile", mobile);
        startActivityForResult(IdentityCodeActivity.class, bundle, CodeUtils.REQUEST_REGISTER_CODE);
    }

    public void requestCodeNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.getRegisterVerify, RequestMethod.GET, null);
        request.add("mobile", mobile);
        executeNetwork(what,"请稍后", request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==CodeUtils.REQUEST_REGISTER_CODE){
            setResult(RESULT_OK,getIntent());
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = s.toString();
        if (str.length() > 0) {
            btnRegister.setEnabled(true);
        } else {
            btnRegister.setEnabled(false);
        }
    }
}
