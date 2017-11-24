package com.ascba.rebate.utils;

import android.content.Context;

import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.view.DebugSelectDialog;

/**
 * Created by Jero on 2017/11/24 0024.
 */

public class Demo {
    private DebugSelectDialog debugSelectDialog;

    public void showTest1(Context context) {
        if (debugSelectDialog == null) {
            debugSelectDialog = new DebugSelectDialog(context);
            debugSelectDialog.setCallback(new DebugSelectDialog.Callback() {
                @Override
                public void go(String type) {
                    AppConfig.getInstance().putString("debug_url", type);
                    debugSelectDialog.dismiss();
                }
            });
        }
        debugSelectDialog.show();
    }
}
