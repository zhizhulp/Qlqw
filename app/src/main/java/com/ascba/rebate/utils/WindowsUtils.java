package com.ascba.rebate.utils;

import android.content.Context;
import android.os.Build;

/**
 * Created by Jero on 2018/1/20 0020.
 */

public class WindowsUtils {

    public static int getStatusBarHeight(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return 0;
        }
        int statusBarHeight;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        else
            statusBarHeight = (int) ScreenDpiUtils.dp2px(context, 24);
        return statusBarHeight;
    }

    public static int getWindowsHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getWindowsWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

}
