package com.ascba.rebate.activities.bank_card;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextHint;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by 李平 on 2017/9/13 13:57
 * Describe: 添加银行卡
 */

public class AddBankCardActivity extends BaseDefaultNetActivity implements TextWatcher, View.OnClickListener {

    private EditTextHint etNum;
    private Button btnCheck;
    private TextView tvName;

    @Override
    protected int bindLayout() {
        return R.layout.activity_add_bank_card;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        etNum = fv(R.id.et_num);
        btnCheck = fv(R.id.btn_check);
        tvName = fv(R.id.tv_name);
        tvName.setText(AppConfig.getInstance().getString("realname", null));
        etNum.addTextChangedListener(this);
        btnCheck.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            btnCheck.setEnabled(true);
        } else {
            btnCheck.setEnabled(false);
        }
    }


    //点击提示
    public void nameAttention(View view) {
        dm.showAlertDialog("为了你的资金安全，只能绑定持卡人本人的信息。", "我知道了", null);
    }

    //点击提交
    @Override
    public void onClick(View v) {
        AbstractRequest request = buildRequest(UrlUtils.verifyBankCard, RequestMethod.POST, null);
        request.add("bank_card", etNum.getText().toString());
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            showToast(result.getMsg());
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {

        }
    }
}
