package com.ascba.rebate.application;

import android.app.Application;
import android.util.Log;

import com.ascba.rebate.utils.CodeUtils;
import com.squareup.leakcanary.LeakCanary;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 李平 on 2017/8/11.
 * 第三方库初始化
 */

public class MyApplication extends Application {
    private static MyApplication app;
    private IWXAPI iwxapi;
    public static int patchStatusCode;//是否需要杀死app去加载补丁

    @Override
    public void onCreate() {
        initHotFix();
        super.onCreate();
        app = this;
        JPushInterface.init(this);//极光推送
        JPushInterface.setDebugMode(true);
        NoHttp.initialize(this);
        Logger.setDebug(true);
        Logger.setTag("NoHttp");
        //内存泄漏检测
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    private void initHotFix() {
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                //.setAesKey("0123456789123456")
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        String TAG ="hotfix";
                        //详细参数参照官方文档
                        String msg = "Mode:" + mode +
                                " Code:" + code +
                                " Info:" + info +
                                " HandlePatchVersion:" + handlePatchVersion;
                        Log.d(TAG, "onLoad: "+ msg);
                        patchStatusCode=code;
                    }
                }).initialize();
    }

    public IWXAPI getWXAPI() {
        if (iwxapi == null) {
            iwxapi = WXAPIFactory.createWXAPI(this, null);
            iwxapi.registerApp(CodeUtils.WX_APP_ID);
        }
        return iwxapi;
    }

    public static MyApplication getInstance() {
        return app;
    }
}
