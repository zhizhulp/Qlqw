package com.ascba.rebate.manager;

import android.widget.Toast;

import com.ascba.rebate.application.MyApplication;

/**
 * Created by Jero on 2017/11/2 0002.
 */

public class ToastManager {

    private static Toast mToast;

    public static void show(CharSequence text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_LONG);
        mToast.show();
    }
}
