package com.ascba.rebate.activities.company_identification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.ComMsg;
import com.ascba.rebate.manager.DialogManager;

/**
 * Created by 李平 on 2017/12/7 16:17
 * Describe: 企业认证成功
 */

public class CISuccessActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private TextView tvName;
    private TextView tvLawName;
    private TextView tvMoney;
    private TextView tvStatus;
    private TextView tvMineName;
    private String scope;

    @Override
    protected int bindLayout() {
        return R.layout.activity_cisuccess;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        tvName = fv(R.id.tv_name);
        tvLawName = fv(R.id.tv_law_name);
        tvMoney = fv(R.id.tv_money);
        tvStatus = fv(R.id.tv_status);

        fv(R.id.lat_scope).setOnClickListener(this);
        fv(R.id.btn_apply).setOnClickListener(this);
        tvMineName = fv(R.id.tv_mine_name);

        getParams();
    }

    private void getParams() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            ComMsg companyMsg = extras.getParcelable("company_msg");
            if (companyMsg != null) {
                setUI(companyMsg);
            }else {
                showToast("数据获取失败。");
                finish();
            }
        } else {
            showToast("数据获取失败。");
            finish();
        }
    }

    private void setUI(ComMsg comMsg) {
        tvName.setText(comMsg.getName());
        tvLawName.setText(comMsg.getOper_name());
        tvMoney.setText(comMsg.getRegist_capi());
        tvStatus.setText(comMsg.getCompany_status());
        tvMineName.setText(comMsg.getClientele_name());
        scope = comMsg.getScope();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lat_scope:
                dm.showAlertDialog(scope, "我知道了", null);
                break;
            case R.id.btn_apply:

                break;
        }
    }
}
