package com.ascba.rebate.base.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ascba.rebate.activities.main.MainActivity;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.manager.ActivityManager;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.manager.ToastManager;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;

/**
 * UI 权限 activity管理 网络监听
 */
public abstract class BaseUIActivity extends AppCompatActivity {
    protected static final int UIMODE_NORMAL = 0;//状态栏着色，占位置(默认)
    protected static final int UIMODE_FULLSCREEN_NOTALL = 1;//全屏,虚拟键透明
    protected static final int UIMODE_FULLSCREEN = 2;//全屏(状态栏透明，虚拟键透明)
    protected static final int UIMODE_TRANSPARENT = 3;//状态栏透明，且不占位置 全透明
    protected static final int UIMODE_TRANSPARENT_NOTALL = 4;//状态栏透明，且不占位置 半透明
    protected static final int UIMODE_TRANSPARENT_FULLSCREEN = 5;//全屏，状态栏半透明，虚拟键半透明，状态栏有文字

    protected int uiMode;
    protected PermissionCallback requestPermissionAndBack;
    protected String TAG = getClass().getSimpleName();
    protected DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//屏幕方向固定为竖屏
        //加入锁屏和home键监听
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mHomeKeyEventReceiver, intentFilter);

        uiMode = setUIMode();
        setUITheme();
        setContentView(bindLayout());
        dm = new DialogManager(this);
        ActivityManager.getInstance().addActivity(this);
        initViews(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mHomeKeyEventReceiver);
        if(MyApplication.patchStatusCode== PatchStatus.CODE_LOAD_RELAUNCH){
            if(this instanceof MainActivity){
                killProcess(false);
            }
        }else {
            if(this instanceof MainActivity){
                System.exit(0);
            }
        }
        ActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    /**
     * 设置主题样式（主要针对是否全屏，状态栏是否透明）
     */
    private void setUITheme() {
        Window window = getWindow();
        if (uiMode == UIMODE_FULLSCREEN) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        } else if (uiMode == UIMODE_FULLSCREEN_NOTALL) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (uiMode == UIMODE_TRANSPARENT || uiMode == UIMODE_TRANSPARENT_NOTALL || uiMode == UIMODE_TRANSPARENT_FULLSCREEN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (uiMode == UIMODE_TRANSPARENT_FULLSCREEN) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (uiMode == UIMODE_TRANSPARENT_NOTALL) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//全透明或半透明的关键(此处为半透明)
                } else if (uiMode == UIMODE_TRANSPARENT_FULLSCREEN) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//全透明或半透明的关键(此处为半透明)
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                }
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    /**
     * 设置状态栏的颜色
     *
     * @param color 要设置的颜色
     */
    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uiMode != UIMODE_TRANSPARENT && uiMode != UIMODE_TRANSPARENT_NOTALL) {
            getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 简化findviewbyid
     *
     * @param resId 控件id
     * @param <T>   view
     * @return view
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T fv(int resId) {
        return (T) findViewById(resId);
    }

    /**
     * 吐司
     *
     * @param content 内容
     */
    public void showToast(String content) {
        ToastManager.show(content);
    }

    /**
     * 绑定视图id    (setContentView());
     *
     * @return 视图id
     */
    protected abstract int bindLayout();

    /**
     * 控件的初始化
     */
    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract int setUIMode();

    public interface PermissionCallback {
        void requestPermissionAndBack(boolean isOk);
    }

    /**
     * 申请权限
     */
    public void checkAndRequestAllPermission(String[] permissions, PermissionCallback requestPermissionAndBack) {
        this.requestPermissionAndBack = requestPermissionAndBack;
        if (permissions == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkAllPermissions(permissions)) {//没有所有的权限
                ActivityCompat.requestPermissions(this, permissions, 1);
            } else {
                if (requestPermissionAndBack != null) {
                    requestPermissionAndBack.requestPermissionAndBack(true);//有权限
                }
            }
        } else {
            if (requestPermissionAndBack != null) {
                requestPermissionAndBack.requestPermissionAndBack(true);//有权限
            }
        }
    }

    private boolean checkAllPermissions(@NonNull String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] per,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            Log.d(TAG, "onRequestPermissionsResult: activity");
            boolean isAll = true;
            for (int i = 0; i < per.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    isAll = false;
                    break;
                }
            }
            if (!isAll) {
                showToast("部分功能可能无法使用，因为你拒绝了一些权限");
            }
            if (requestPermissionAndBack != null) {
                requestPermissionAndBack.requestPermissionAndBack(isAll);//isAll 用户是否拥有所有权限
            }
        }
        super.onRequestPermissionsResult(requestCode, per, grantResults);
    }
    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            if(MyApplication.patchStatusCode== PatchStatus.CODE_LOAD_RELAUNCH){
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                    String reason = intent.getStringExtra(SYSTEM_REASON);
                    if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                        killProcess(true);
                    }
                }else if(action.equals(Intent.ACTION_SCREEN_OFF)){
                    killProcess(true);
                }
            }
        }
    };
    //杀进程，用于加载补丁
    private void killProcess(boolean needReboot){
        SophixManager.getInstance().killProcessSafely();
        if(needReboot){
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }
}
