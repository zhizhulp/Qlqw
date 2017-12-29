package com.ascba.rebate.activities.trade;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.qr.camera.CameraManager;
import com.ascba.rebate.view.qr.decode.CaptureActivityHandler;
import com.ascba.rebate.view.qr.decode.DecodeManager;
import com.ascba.rebate.view.qr.decode.InactivityTimer;
import com.ascba.rebate.view.qr.view.QrCodeFinderView;
import com.yanzhenjie.nohttp.RequestMethod;

import java.io.IOException;

import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;

import static com.ascba.rebate.utils.CodeUtils.REQUEST_ALBUM_ICON;

/**
 * Created by 李平 on 2017/10/12 14:59
 * Describe: 扫一扫界面
 */

public class SweepActivity extends BaseDefaultNetActivity implements SurfaceHolder.Callback {
    private CheckBox switchLight;
    private String sellerID;
    private CaptureActivityHandler mCaptureActivityHandler;
    private boolean mHasSurface;
    private InactivityTimer mInactivityTimer;
    private QrCodeFinderView mQrCodeFinderView;
    private SurfaceView mSurfaceView;
    private DecodeManager mDecodeManager = new DecodeManager();

    @Override
    protected int bindLayout() {
        return R.layout.activity_sweep;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        switchLight = fv(R.id.rb_switch_light);
        switchLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchLight.isChecked()) {
                    turnFlashlightOn();
                } else {
                    turnFlashLightOff();
                }
            }
        });
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                checkAndRequestAllPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionCallback() {
                    @Override
                    public void requestPermissionAndBack(boolean isOk) {
                        if (isOk) {
                            Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent2, REQUEST_ALBUM_ICON);
                        }
                    }
                });

            }
        });
        initView();
        initData();
    }

    private void requestPermissionInitCamera() {
        checkAndRequestAllPermission(new String[]{Manifest.permission.CAMERA}, new BaseUIActivity.PermissionCallback() {
            @Override
            public void requestPermissionAndBack(boolean isOk) {
                if (isOk) {
                    initCamera();
                }else {
                    finish();
                }
            }
        });
    }

    private void initView() {
        mQrCodeFinderView = (QrCodeFinderView) findViewById(R.id.qr_code_view_finder);
        mSurfaceView = (SurfaceView) findViewById(R.id.qr_code_view_stub);
        mHasSurface = false;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ALBUM_ICON && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                final String picturePath = cursor.getString(columnIndex);
                cursor.close();
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        return QRCodeDecoder.syncDecodeQRCode(picturePath);
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        if (TextUtils.isEmpty(result)) {
                            showToast("未发现二维码");
                        } else {
                            if (result.contains("qlqw")) {
                                handlerCode(result);
                            } else {
                                showToast("非有效二维码");
                            }
                        }
                    }
                }.execute();
            }
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            String data = (String) result.getData();
            JSONObject dataObj = JSON.parseObject(data);
            JSONObject infoObj = dataObj.getJSONObject("info");
            Bundle b = new Bundle();
            b.putString("seller_id", sellerID);
            b.putString("seller_cover_logo", infoObj.getString("seller_cover_logo"));
            b.putString("seller_name", infoObj.getString("seller_name"));
            b.putString("self_money", infoObj.getString("self_money"));
            startActivity(OfflinePayActivity.class, b);
            finish();
        }
    }

    private void handlerCode(String result) {
        AbstractRequest request = buildRequest(UrlUtils.checkSeller, RequestMethod.POST, null);
        //sellerID = result.split("seller_flag=")[1].split("&")[0];
        String[] split1 = result.split("seller_flag=");
        if (split1 != null && split1.length >= 2) {
            String s = split1[1];
            if (s != null) {
                String[] split2 = s.split("&");
                if (split2 != null && split2.length >= 1) {
                    sellerID = split2[0];
                }else {
                    showToast(getString(R.string.code_unavailable));
                    return;
                }
            }else {
                showToast(getString(R.string.code_unavailable));
                return;
            }
        }else {
            showToast(getString(R.string.code_unavailable));
            return;
        }
        request.add("seller", sellerID);
        executeNetwork(0, "请稍后", request);
    }

    private void initData() {
        CameraManager.init(this);
        mInactivityTimer = new InactivityTimer(this);
    }

    /**
     * Handler scan result
     */
    public void handleDecode(com.google.zxing.Result result) {
        mInactivityTimer.onActivity();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        if (null == result) {
            restartPreview();
        } else {
            String resultString = result.getText();
            if (resultString.contains("qlqw")) {
                handlerCode(resultString);
            } else {
                showToast(getString(R.string.code_unavailable));
                restartPreview();
            }
        }
    }

    private void initCamera() {
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (mHasSurface) {
            openCamera(surfaceHolder);
        } else {
            Log.d(TAG, "surfaceHolder.addCallback");
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    private void closeCamera() {
        if (mCaptureActivityHandler != null) {
            try {
                mCaptureActivityHandler.quitSynchronously();
                mCaptureActivityHandler = null;
                mHasSurface = false;
                if (null != mSurfaceView) {
                    mSurfaceView.getHolder().removeCallback(this);
                }
                CameraManager.get().closeDriver();
                Log.d(TAG, "closeCamera: ");
            } catch (Exception e) {
                // 关闭摄像头失败的情况下,最好退出该Activity,否则下次初始化的时候会显示摄像头已占用.
                finish();
            }
        }
    }

    private void openCamera(SurfaceHolder surfaceHolder) {
        try {
            if (!CameraManager.get().openDriver(surfaceHolder)) {
                showPermissionDeniedDialog();
                return;
            }
        } catch (IOException e) {
            // 基本不会出现相机不存在的情况
            Toast.makeText(this, "没有找到相机", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } catch (RuntimeException re) {
            re.printStackTrace();
            showPermissionDeniedDialog();
            return;
        }
        mQrCodeFinderView.setVisibility(View.VISIBLE);
        if (mCaptureActivityHandler == null) {
            mCaptureActivityHandler = new CaptureActivityHandler(this);
        }
        Log.d(TAG, "openCamera: ");
    }

    private void showPermissionDeniedDialog() {
        mQrCodeFinderView.setVisibility(View.GONE);
        mDecodeManager.showPermissionDeniedDialog(this);
    }

    private void turnFlashlightOn() {
        try {
            CameraManager.get().setFlashLight(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void turnFlashLightOff() {
        try {
            CameraManager.get().setFlashLight(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restartPreview() {
        if (null != mCaptureActivityHandler) {
            try {
                mCaptureActivityHandler.restartPreviewAndDecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Handler getCaptureActivityHandler() {
        return mCaptureActivityHandler;
    }
    @Override
    protected void onResume() {
        super.onResume();
        requestPermissionInitCamera();
        mSurfaceView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeCamera();
        mSurfaceView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        if (null != mInactivityTimer) {
            mInactivityTimer.shutdown();
        }
        super.onDestroy();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged: width " + width + ",height " + height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated: ");
        if (!mHasSurface) {
            mHasSurface = true;
            openCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed: ");
        mHasSurface = false;
    }

    @Override
    protected void mHandle404(int what, Result result) {
        super.mHandle404(what, result);
        restartPreview();
    }

    @Override
    protected void mHandleFailed(int what) {
        super.mHandleFailed(what);
        restartPreview();
    }

    @Override
    protected void mHandleReLogin(int what, Result result) {
        super.mHandleReLogin(what, result);
        finish();
    }

    @Override
    protected void mHandleNoNetwork(int what) {
        super.mHandleNoNetwork(what);
        restartPreview();
    }
}
