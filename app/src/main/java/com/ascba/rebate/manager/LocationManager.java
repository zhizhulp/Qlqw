package com.ascba.rebate.manager;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ascba.rebate.base.activity.BaseUIActivity;

/**
 * 定位管理类
 */
public class LocationManager implements BaseUIActivity.PermissionCallback {
    private Activity activity;
    private LocateListener locateListener;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String[] locatePer = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};
    private String TAG = "LocationManager";

    public LocationManager(Activity activity, LocateListener locateListener) {
        this.activity = activity;
        this.locateListener = locateListener;
        if (activity instanceof BaseUIActivity) {
            ((BaseUIActivity) activity).checkAndRequestAllPermission(locatePer, this);
        }
    }

    @Override
    public void requestPermissionAndBack(boolean isOk) {
        if (isOk) {
            startLocation();
        }
    }

    public interface LocateListener {
        void onLocateSuccess(AMapLocation location);
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        locationClient = new AMapLocationClient(activity);
        initDefaultOption();
        locationClient.setLocationOption(locationOption);
        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();
    }

    /**
     * 默认的定位参数
     */
    private void initDefaultOption() {
        locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        locationOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        locationOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        locationOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        locationOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        locationOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        locationOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        locationOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        locationOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        locationOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
    }

    /**
     * 定位监听
     */
    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    Log.d(TAG, "locate success.");
                    stopLocation();
                    if (locateListener != null) {
                        locateListener.onLocateSuccess(location);
                    }
                } else {
                    Log.e(TAG, "locate error,error code :" + location.getErrorCode() + ",error msg" + location.getErrorInfo());
                }
            } else {
                Log.e(TAG, "locate error");
            }
        }
    };

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        if (null != locationClient)
            locationClient.stopLocation();
    }

    /**
     * 销毁定位
     * 如果AMapLocationClient是在当前Activity实例化的，
     * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
     * @author hongming.wang
     * @since 2.8.0
     */
    public void destroyLocation() {
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
}
