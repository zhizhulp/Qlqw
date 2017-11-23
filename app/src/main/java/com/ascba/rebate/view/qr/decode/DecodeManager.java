package com.ascba.rebate.view.qr.decode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.ascba.rebate.R;


/**
 * Created by xingli on 1/8/16.
 * <p/>
 * 二维码解析管理。
 */
public class DecodeManager {

    public void showPermissionDeniedDialog(final Context context) {
        // 权限在安装时被关闭了，如小米手机
        new AlertDialog.Builder(context).setTitle(R.string.qr_code_notification)
                .setMessage(R.string.qr_code_camera_not_open)
                .setPositiveButton(R.string.qr_code_positive_button_know, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }
                })
                .show();
    }
}
