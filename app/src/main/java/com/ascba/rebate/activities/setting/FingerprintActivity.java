package com.ascba.rebate.activities.setting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultUIActivity;
import com.ascba.rebate.manager.DialogManager;
import com.suke.widget.SwitchButton;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

/**
 * Created by Jero on 2017/9/16 0016.
 */

public class FingerprintActivity extends BaseDefaultUIActivity {

    private static final int SET_FPR = 745;

    private SwitchButton switchButton;
    private AppConfig appConfig;

    private boolean isClose = false;
    private Dialog touchDialog;
    private FingerprintIdentify mFingerprintIdentify;

    @Override
    protected int bindLayout() {
        return R.layout.activity_fingerprint;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        setResult(RESULT_OK);
        appConfig = AppConfig.getInstance();
        switchButton = fv(R.id.fingerprint_switch);
        switchButton.setChecked(appConfig.getInt("is_fpr", 0) == 1);
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {//开
                    startFingerprint();
                } else {//关
                    appConfig.putInt("is_fpr", 0);
                }
            }
        });
        initFingerprint();
    }

    private void initFingerprint() {
        mFingerprintIdentify = new FingerprintIdentify(this);
        if (!mFingerprintIdentify.isHardwareEnable()) {
            dm.showAlertDialog("暂不支持该设备的指纹识别功能", "关闭", new DialogManager.Callback() {
                @Override
                public void handleLeft() {
                    finish();
                }
            });
        } else if (!mFingerprintIdentify.isRegisteredFingerprint()) {
            if (Build.BRAND.equals("Xiaomi"))
                dm.showAlertDialog("还没有设置指纹，请去“设置”->“锁屏、密码和指纹”添加指纹", "关闭", new DialogManager.Callback() {
                    @Override
                    public void handleLeft() {
                        finish();
                    }
                });
            else
                dm.showAlertDialog2("还没有设置指纹，请去“设置”->“安全”添加指纹", "下次", "去设置", new DialogManager.Callback() {
                    @Override
                    public void handleLeft() {
                        finish();
                    }

                    @Override
                    public void handleRight() {
                        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                        startActivity(intent);
                        finish();
                    }
                });
        }
    }

    private void startFingerprint() {
        if (mFingerprintIdentify.isFingerprintEnable()) {
            startIdentify();
            touchDialog = dm.showFingerprint(new DialogManager.Callback() {
                @Override
                public void handleLeft() {
                    switchButton.setChecked(false);
                    stopFingerprint();
                }
            });
        } else {
            showToast("该设备暂不支持!");
            finish();
        }
    }

    private void stopFingerprint() {
        isClose = true;
        mFingerprintIdentify.cancelIdentify();
    }

    private void startIdentify() {
        if (isClose) {
            isClose = false;
            mFingerprintIdentify.resumeIdentify();
        } else
            mFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
                @Override
                public void onSucceed() {
                    touchDialog.dismiss();
                    appConfig.putInt("is_fpr", 1);
                    // 验证支付密码
                }

                @Override
                public void onNotMatch(int availableTimes) {
                    showToast("剩余" + availableTimes + "次机会");
                }

                @Override
                public void onFailed(boolean isDeviceLocked) {
                    if (isDeviceLocked) showToast("错误次数过多，指纹设备暂被锁定！");
                    else showToast("错误次说过多，请使用正确的指纹验证！");
                    touchDialog.dismiss();
                    switchButton.setChecked(false);
                }

                @Override
                public void onStartFailedByDeviceLocked() {
                    showToast("错误次数过多，指纹设备暂被锁定！");
                    touchDialog.dismiss();
                    switchButton.setChecked(false);
                }
            });
    }
}
