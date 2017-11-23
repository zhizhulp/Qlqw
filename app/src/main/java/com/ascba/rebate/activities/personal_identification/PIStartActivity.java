package com.ascba.rebate.activities.personal_identification;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.megvii.livenesslib.util.ConUtil;
import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;

/**
 * 个人认证第一步
 */
public class PIStartActivity extends BaseDefaultNetActivity {
    private String uuid;
    private Button btnStart;

    @Override
    protected int bindLayout() {
        return R.layout.activity_per_iden_step1;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        init();
        netWorkWarranty();
    }

    private void init() {
        btnStart = fv(R.id.btn_start);
        uuid = ConUtil.getUUIDString(this);
    }

    public void startCheck(View view) {
        checkAndRequestAllPermission(new String[]{Manifest.permission.CAMERA}, new PermissionCallback() {
            @Override
            public void requestPermissionAndBack(boolean isOk) {
                if (isOk) {
                    startActivity(LivenessActivity.class, null);
                    finish();
                } else {
                    showToast("请到应用设置中打开相机权限！");
                }
            }
        });
    }

    /**
     * 联网授权
     */
    private void netWorkWarranty() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(PIStartActivity.this);
                LivenessLicenseManager licenseManager = new LivenessLicenseManager(PIStartActivity.this);
                manager.registerLicenseManager(licenseManager);

                manager.takeLicenseFromNetwork(uuid);
                if (licenseManager.checkCachedLicense() > 0)
                    mHandler.sendEmptyMessage(1);
                else
                    mHandler.sendEmptyMessage(2);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    btnStart.setEnabled(true);
                    break;
                case 2:
                    btnStart.setEnabled(false);
                    showToast(getString(R.string.net_authen_failed));
                    break;
            }
        }
    };
}
