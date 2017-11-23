package com.ascba.rebate.activities.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.EncodeUtils;
import com.ascba.rebate.utils.RegexUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextHint;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * 注册界面（填写昵称等）
 */
public class RegisterNextActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private EditTextHint etNickName, etPsd, etConPsd, etRecId;
    private Button btnRegister;
    private String mobile;
    private int code;

    @Override
    protected int bindLayout() {
        return R.layout.activity_register_next;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        getParams();
        etNickName = fv(R.id.et_nick_name);
        etPsd = fv(R.id.et_psd);
        etConPsd = fv(R.id.et_confirm_psd);
        etRecId = fv(R.id.et_rec_id);

        btnRegister = fv(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        etNickName.addTextChangedListener(new IdTextWatcher());
        etPsd.addTextChangedListener(new IdTextWatcher());
        etConPsd.addTextChangedListener(new IdTextWatcher());

        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickBack(View back) {
                showAlertDlg();
            }
        });
    }

    private void getParams() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mobile = extras.getString("mobile");
            code = extras.getInt("code");
        }
    }

    @Override
    public void onClick(View v) {
        if (checkData()) {
            requestNetwork();
        }
    }

    private boolean checkData() {
        String strNickname = etNickName.getText().toString();
        String strPsd = etPsd.getText().toString();
        String strConPsd = etConPsd.getText().toString();
        boolean ncRight = strNickname.length() >= 1 && strNickname.length() <= 11;
        boolean psdRight = strPsd.length() == 6;
        if (!ncRight) {
            showToast(getString(R.string.name_unavailable));
            return false;
        }else {
            if(!RegexUtils.isUserName(strNickname)){
                showToast(getString(R.string.input_name_unavailable));
                return false;
            }
        }
        if (!psdRight) {
            showToast(getString(R.string.pay_psd_unavailable));
            return false;
        }
        if (strPsd.equals(strConPsd)) {
            return true;
        } else {
            showToast(getString(R.string.input_not_same));
            return false;
        }
    }

    private void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.register, RequestMethod.POST, null);
        request.add("mobile", mobile);
        request.add("verify", code);
        request.add("nickname", etNickName.getText().toString());
        request.add("pay_password", EncodeUtils.encryptPsd(etPsd.getText().toString()));
        request.add("repay_password", EncodeUtils.encryptPsd(etConPsd.getText().toString()));
        request.add("p_referee", etRecId.getText().toString());
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            showToast(result.getMsg());
            setResult(RESULT_OK, new Intent(this,IdentityCodeActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        showAlertDlg();
    }

    private void showAlertDlg() {
        dm.showAlertDialog2(getString(R.string.cancel_register), null, null, new DialogManager.Callback() {
            @Override
            public void handleLeft() {
                setResult(RESULT_CANCELED, getIntent());
                finish();
            }
        });
    }

    @Override
    protected int setUIMode() {
        return UIMODE_TRANSPARENT_FULLSCREEN;
    }


    private class IdTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String strNickname = etNickName.getText().toString();
            String strPsd = etPsd.getText().toString();
            String strConPsd = etConPsd.getText().toString();
            if (!strNickname.equals("") && !strPsd.equals("") && !strConPsd.equals("")) {
                btnRegister.setEnabled(true);
            } else {
                btnRegister.setEnabled(false);
            }
        }
    }

}
