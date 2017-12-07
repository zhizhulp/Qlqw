package com.ascba.rebate.activities.merchant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.MctModType;
import com.ascba.rebate.view.EditTextHint;

public class MctModBaseActivity extends BaseDefaultNetActivity implements TextWatcher, View.OnClickListener {

    private MctModType mmType;
    private EditTextHint etInput;
    private Button btnSave;

    @Override
    protected int bindLayout() {
        return R.layout.activity_mct_mod_base;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        getParams();
    }

    private void getParams() {
        Intent intent = getIntent();
        mmType = intent.getParcelableExtra("mm_type");
        if (mmType == null) {
            finish();
        }else {
            initView();
        }
    }

    private void initView() {
        ((TextView) fv(R.id.tv_title)).setText(mmType.getTitle());
        ((TextView) fv(R.id.tv_rules)).setText(mmType.getRules());
        btnSave = fv(R.id.btn_save);
        btnSave.setOnClickListener(this);
        etInput = fv(R.id.et_input);
        etInput.addTextChangedListener(this);
        etInput.setHint(mmType.getHint());
        etInput.setText(mmType.getContent());
        etInput.setSelection(mmType.getContent().length());
    }

    public static void start(Activity context, MctModType type) {
        Intent intent = new Intent(context, MctModBaseActivity.class);
        intent.putExtra("mm_type", type);
        context.startActivityForResult(intent, type.getRequestCode());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length()>0){
            btnSave.setEnabled(true);
        }else {
            btnSave.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        intent.putExtra("value",etInput.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }
}
