package com.ascba.rebate.activities.user_data;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.utils.RegexUtils;
import com.ascba.rebate.view.EditTextHint;

/**
 * 修改昵称
 */
public class ModifyNickNameActivity extends BaseDefaultNetActivity implements View.OnClickListener, TextWatcher {

    private EditTextHint etNickName;
    private Button btnSave;

    @Override
    protected int bindLayout() {
        return R.layout.activity_modify_nick_name;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        btnSave = fv(R.id.btn_save);
        btnSave.setOnClickListener(this);
        etNickName = fv(R.id.et_nick_name);
        etNickName.addTextChangedListener(this);
        etNickName.setText(AppConfig.getInstance().getString("nickname", null));
        etNickName.requestFocus(etNickName.length());
    }

    @Override
    public void onClick(View v) {
        if (etNickName.length() >= 1 && etNickName.length() <= 11) {
            if (RegexUtils.isUserName(etNickName.getText().toString())) {
                Intent intent = getIntent();
                intent.putExtra("nick_name", etNickName.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                showToast(getString(R.string.input_name_unavailable));
            }
        } else {
            showToast(getString(R.string.name_unavailable));
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
        int len = str.length();
        if (len > 0) {
            btnSave.setEnabled(true);
        } else {
            btnSave.setEnabled(false);
        }
    }

}
