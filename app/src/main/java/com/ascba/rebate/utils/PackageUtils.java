package com.ascba.rebate.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import com.ascba.rebate.application.MyApplication;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by 李平 on 2017/8/14.
 * 主要用于获取版本信息
 */

public class PackageUtils {
    /**
     * 获取app当前版本号
     */
    public static String getPackageVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }

    public static String getAndroidVersion(){
        return Build.VERSION.RELEASE;
    }

    /**
     *  获取设备唯一标识
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId() {
        String androidId = Settings.Secure.getString(MyApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            if (!"9774d56d682e549c".equals(androidId)) {
                return UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
            } else {
                return UUID.randomUUID().toString();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPacageName(Context context){
        return context.getPackageName();
    }

    /**
     * 判断app是否是调试版
     * @param context 上下文
     * @return boolean
     */
    public static boolean isAppDebug(Context context){
        try {
            ApplicationInfo info= context.getApplicationInfo();
            return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
