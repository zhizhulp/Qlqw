package com.ascba.rebate.activities.mine;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.setting.AboutActivity;
import com.ascba.rebate.activities.setting.AccountActivity;
import com.ascba.rebate.activities.setting.AddressActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.manager.ActivityManager;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.utils.DataCleanUtils;

public class SettingActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private RelativeLayout rlAccount, rlClean, rlAbout, rlAddress;
    private Button btnLogout;
    private TextView tvCleanNum;

    @Override
    protected int bindLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        tvCleanNum = fv(R.id.setting_clean_num);
        tvCleanNum.setText(DataCleanUtils.getApplicationDataSize(this));
        rlAccount = fv(R.id.account_rl);
        rlClean = fv(R.id.clean_rl);
        rlAbout = fv(R.id.about_rl);
        rlAddress = fv(R.id.address_rl);
        rlAccount.setOnClickListener(this);
        rlClean.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
        rlAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.address_rl:
                startActivity(AddressActivity.class, null);
                break;
            case R.id.account_rl:
                startActivity(AccountActivity.class, null);
                break;
            case R.id.clean_rl:
                if (tvCleanNum.getText().equals("0K")) {
                    showToast("无缓存");
                    return;
                }
                ProgressDialog progressDialog = ProgressDialog.show(this, null, "正在清理缓存...", true, true);
                DataCleanUtils.cleanApplicationData(this);
                tvCleanNum.setText(DataCleanUtils.getApplicationDataSize(this));
                progressDialog.dismiss();
                dm.showAlertDialog("缓存已清除", "确认", null);
                break;
            case R.id.about_rl:
                startActivity(AboutActivity.class, null);
                break;
        }
    }

    public void logout(View view) {
        dm.showAlertDialog2("您确定要退出登陆吗?", null, null, new DialogManager.Callback() {
            @Override
            public void handleLeft() {
                AppConfig.getInstance().putBoolean("first_login",true);
                setResult(RESULT_OK);
                finish();
                startActivity(LoginActivity.class,null);
            }
        });
    }
}
