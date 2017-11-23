package com.ascba.rebate.activities.company_identification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.ComMsg;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ClearEditText;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * 输入公司名称
 */
public class InPutCNActivity extends BaseDefaultNetActivity implements TextWatcher, View.OnClickListener {

    private Button btnNext;
    private ClearEditText etComName;

    @Override
    protected int bindLayout() {
        return R.layout.activity_in_put_cn;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        etComName = fv(R.id.et_com_name);
        etComName.addTextChangedListener(this);

        btnNext = fv(R.id.btn_next);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AbstractRequest request = buildRequest(UrlUtils.findCompanyInfo, RequestMethod.GET, ComMsg.class);
        request.add("company_name",etComName.getText().toString());
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            ComMsg comMsg = (ComMsg) result.getData();
            Bundle b = new Bundle();
            b.putParcelable("company_msg", comMsg);
            startActivityForResult(ComMsgActivity.class, b, CodeUtils.REQUEST_COMPANY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CodeUtils.REQUEST_COMPANY && resultCode==RESULT_OK){
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
        String s1 = s.toString();
        if (s1.length() > 0) {
            btnNext.setEnabled(true);
        } else {
            btnNext.setEnabled(false);
        }
    }
}
