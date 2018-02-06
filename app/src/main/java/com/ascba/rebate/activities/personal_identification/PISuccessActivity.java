package com.ascba.rebate.activities.personal_identification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.PIUserInfo;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * 个人认证成功
 */
public class PISuccessActivity extends BaseDefaultNetActivity {

    private TextView tvName;
    private TextView tvAge;
    private TextView tvSex;
    private TextView tvCardNum;
    private TextView tvLocation;
    private View btnComplete;

    @Override
    protected int bindLayout() {
        return R.layout.activity_pisuccess;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        tvName = fv(R.id.tv_name);
        tvAge = fv(R.id.tv_age);
        tvSex = fv(R.id.tv_sex);
        tvCardNum = fv(R.id.tv_card_num);
        tvLocation = fv(R.id.tv_location);
        btnComplete = fv(R.id.btn_complete);
        getParams();
        AbstractRequest request = buildRequest(UrlUtils.nameSeek, RequestMethod.POST, PIUserInfo.class);
        executeNetwork(0, "请稍后", request);
    }

    private void getParams() {
        btnComplete.setVisibility(getIntent().getBooleanExtra("show_btn", true) ?
                View.VISIBLE: View.GONE);
    }

    public static void start(Context context, boolean showBtn) {
        Intent intent = new Intent(context, PISuccessActivity.class);
        intent.putExtra("show_btn", showBtn);
        context.startActivity(intent);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            PIUserInfo pi = (PIUserInfo) result.getData();
            tvName.setText(pi.getRealname());
            tvAge.setText(String.valueOf(pi.getAge()));
            tvSex.setText(pi.getSex());
            tvCardNum.setText(pi.getCardid());
            tvLocation.setText(pi.getLocation());
            AppConfig.getInstance().putString("realname", pi.getRealname());
        }
    }

    public void close(View view) {
        finish();
    }
}
