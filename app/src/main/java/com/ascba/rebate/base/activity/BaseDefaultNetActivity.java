package com.ascba.rebate.base.activity;

import android.os.Bundle;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.DownloadManager;

/**
 * 默认的网络实现
 */
public class BaseDefaultNetActivity extends BaseNetActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        updateApp(result);
    }

    @Override
    protected void mHandle404(int what, Result result) {
        showToast(result.getMsg());
        updateApp(result);
    }


    @Override
    protected void mHandleReLogin(final int what, Result result) {
        AppConfig.getInstance().putBoolean("first_login", true);
        dm.showAlertDialog2(result.getMsg(), null, null, new DialogManager.Callback() {
            @Override
            public void handleLeft() {
                startActivityForResult(LoginActivity.class, null, what);
            }
        });
    }

    @Override
    protected void mHandleFailed(int what) {
        showToast(getString(R.string.request_failed));
    }

    @Override
    protected void mHandleNoNetwork(int what) {
        showToast(getString(R.string.no_network));
    }

    private void updateApp(Result result) {
        Result.VersionUpgrade upgrade = result.getVersion_upgrade();
        int androidType = upgrade.getAndroid_type();
        if (androidType == 1) {//不强制升级
            boolean showUpdate = AppConfig.getInstance().getBoolean("need_show_update", true);
            if (showUpdate) {
                DownloadManager download = new DownloadManager(this, false);
                download.requestAndDown();
            }
        } else if (androidType == 2) {//强制升级
            DownloadManager download = new DownloadManager(this, true);
            download.requestAndDown();
        }
    }

}
