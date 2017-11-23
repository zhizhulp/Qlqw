package com.ascba.rebate.activities.personal_identification;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.utils.CodeUtils;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.DetectionConfig;
import com.megvii.livenessdetection.DetectionFrame;
import com.megvii.livenessdetection.Detector;
import com.megvii.livenessdetection.Detector.DetectionFailedType;
import com.megvii.livenessdetection.Detector.DetectionListener;
import com.megvii.livenessdetection.Detector.DetectionType;
import com.megvii.livenessdetection.FaceQualityManager;
import com.megvii.livenessdetection.FaceQualityManager.FaceQualityErrorType;
import com.megvii.livenessdetection.bean.FaceIDDataStruct;
import com.megvii.livenessdetection.bean.FaceInfo;
import com.megvii.livenesslib.FaceMask;
import com.megvii.livenesslib.util.ConUtil;
import com.megvii.livenesslib.util.DialogUtil;
import com.megvii.livenesslib.util.ICamera;
import com.megvii.livenesslib.util.IDetection;
import com.megvii.livenesslib.util.IMediaPlayer;
import com.megvii.livenesslib.util.Screen;
import com.megvii.livenesslib.util.SensorUtil;
import com.megvii.livenesslib.view.CircleProgressBar;
import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.CallServer;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.ByteArrayBinary;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LivenessActivity extends BaseDefaultNetActivity
        implements PreviewCallback, DetectionListener, TextureView.SurfaceTextureListener {

    private TextureView camerapreview;
    private FaceMask mFaceMask;// 画脸位置的类（调试时会用到）
    private LinearLayout headViewLinear;// "请在光线充足的情况下进行检测"这个视图
    private TextView timeOutText;
    private RelativeLayout timeOutRel;
    private CircleProgressBar mCircleProgressBar;

    private Detector mDetector;// 活体检测器
    private ICamera mICamera;// 照相机工具类
    private Handler mainHandler;
    private HandlerThread mHandlerThread = new HandlerThread("videoEncoder");
    private IMediaPlayer mIMediaPlayer;// 多媒体工具类
    private IDetection mIDetection;
    private DialogUtil mDialogUtil;

    private TextView promptText;
    private boolean isHandleStart;// 是否开始检测
    private FaceQualityManager mFaceQualityManager;
    private SensorUtil sensorUtil;

    @Override
    protected int bindLayout() {
        return com.megvii.livenesslib.R.layout.liveness_layout;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        init();
        initData();
    }

    @Override
    protected int setUIMode() {
        return UIMODE_FULLSCREEN;
        //return super.setUIMode();
    }

    private void init() {
        sensorUtil = new SensorUtil(this);
        Screen.initialize(this);
        mainHandler = new Handler();
        mHandlerThread.start();
        mIMediaPlayer = new IMediaPlayer(this);
        mDialogUtil = new DialogUtil(this);
        RelativeLayout rootView = (RelativeLayout) findViewById(com.megvii.livenesslib.R.id.liveness_layout_rootRel);
        mIDetection = new IDetection(this, rootView);
        mFaceMask = (FaceMask) findViewById(com.megvii.livenesslib.R.id.liveness_layout_facemask);
        mICamera = new ICamera();
        promptText = (TextView) findViewById(com.megvii.livenesslib.R.id.liveness_layout_promptText);
        camerapreview = (TextureView) findViewById(com.megvii.livenesslib.R.id.liveness_layout_textureview);
        camerapreview.setSurfaceTextureListener(this);
        headViewLinear = (LinearLayout) findViewById(com.megvii.livenesslib.R.id.liveness_layout_bottom_tips_head);
        headViewLinear.setVisibility(View.VISIBLE);
        timeOutRel = (RelativeLayout) findViewById(com.megvii.livenesslib.R.id.detection_step_timeoutRel);
        timeOutText = (TextView) findViewById(com.megvii.livenesslib.R.id.detection_step_timeout_garden);
        mCircleProgressBar = (CircleProgressBar) findViewById(com.megvii.livenesslib.R.id.detection_step_timeout_progressBar);

        mIDetection.viewsInit();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 初始化活体检测器
        DetectionConfig config = new DetectionConfig.Builder().build();
        mDetector = new Detector(this, config);
        boolean initSuccess = mDetector.init(this, ConUtil.readModel(this), "");
        if (!initSuccess) {
            mDialogUtil.showDialog(getString(com.megvii.livenesslib.R.string.meglive_detect_initfailed));
        }

        // 初始化动画
        new Thread(new Runnable() {
            @Override
            public void run() {
                mIDetection.animationInit();
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isHandleStart = false;
        // 打开照相机
        Camera mCamera = mICamera.openCamera(this);
        if (mCamera != null) {
            CameraInfo cameraInfo = new CameraInfo();
            Camera.getCameraInfo(1, cameraInfo);
            mFaceMask.setFrontal(cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT);
            // 获取到相机分辨率对应的显示大小，并把这个值复制给camerapreview
            RelativeLayout.LayoutParams layout_params = mICamera.getLayoutParam();
            camerapreview.setLayoutParams(layout_params);
            mFaceMask.setLayoutParams(layout_params);
            // 初始化人脸质量检测管理类
            mFaceQualityManager = new FaceQualityManager(1 - 0.5f, 0.5f);
            mIDetection.mCurShowIndex = -1;
        } else {
            mDialogUtil.showDialog(getString(com.megvii.livenesslib.R.string.meglive_camera_initfailed));
        }
    }

    /**
     * 开始检测
     */
    private void handleStart() {
        if (isHandleStart)
            return;
        isHandleStart = true;
        // 开始动画
        Animation animationIN = AnimationUtils.loadAnimation(LivenessActivity.this, com.megvii.livenesslib.R.anim.liveness_rightin);
        Animation animationOut = AnimationUtils.loadAnimation(LivenessActivity.this, com.megvii.livenesslib.R.anim.liveness_leftout);
        headViewLinear.startAnimation(animationOut);
        mIDetection.mAnimViews[0].setVisibility(View.VISIBLE);
        mIDetection.mAnimViews[0].startAnimation(animationIN);
        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                timeOutRel.setVisibility(View.VISIBLE);
            }
        });
        // 开始活体检测
        mainHandler.post(mTimeoutRunnable);
    }

    private Runnable mTimeoutRunnable = new Runnable() {
        @Override
        public void run() {
            // 倒计时开始
            initDetecteSession();
            if (mIDetection.mDetectionSteps != null)
                changeType(mIDetection.mDetectionSteps.get(0), 10);
        }
    };

    private void initDetecteSession() {
        if (mICamera.mCamera == null)
            return;
        mIDetection.detectionTypeInit();
        mCurStep = 0;
        mDetector.reset();
        mDetector.changeDetectionType(mIDetection.mDetectionSteps.get(0));
    }

    /**
     * 照相机预览数据回调 （PreviewCallback的接口回调方法）
     */
    @Override
    public void onPreviewFrame(final byte[] data, Camera camera) {
        Size previewsize = camera.getParameters().getPreviewSize();
        // 活体检测器检测
        mDetector.doDetection(data, previewsize.width, previewsize.height, 360 - mICamera.getCameraAngle(this));
    }

    /**
     * 活体验证成功 （DetectionListener的接口回调方法）
     */
    @Override
    public DetectionType onDetectionSuccess(final DetectionFrame validFrame) {
        mIMediaPlayer.reset();
        mCurStep++;
        mFaceMask.setFaceInfo(null);

        if (mCurStep == mIDetection.mDetectionSteps.size()) {
            getLivenessData();
        } else
            changeType(mIDetection.mDetectionSteps.get(mCurStep), 10);

        // 检测器返回值：如果不希望检测器检测则返回DetectionType.DONE，如果希望检测器检测动作则返回要检测的动作
        return mCurStep >= mIDetection.mDetectionSteps.size() ? DetectionType.DONE
                : mIDetection.mDetectionSteps.get(mCurStep);
    }

    private void getLivenessData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final FaceIDDataStruct idDataStruct = mDetector.getFaceIDDataStruct();
                for (String key : idDataStruct.images.keySet()) {
                    final byte[] data = idDataStruct.images.get(key);
                    if (key.equals("image_best")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StringRequest sr = new StringRequest(UrlUtils.detect, RequestMethod.POST);
                                sr.add("api_key", CodeUtils.AUTH_API_KEI);
                                sr.add("api_secret", CodeUtils.AUTH_API_SECRET);
                                sr.add("image", new ByteArrayBinary(data, "qlqw19920128.png"));
                                final ProgressDialog dialog = new ProgressDialog(LivenessActivity.this, R.style.dialog);
                                dialog.setMessage("请稍后");
                                CallServer.getInstance().addRequest(0, sr, new OnResponseListener<String>() {
                                    @Override
                                    public void onStart(int what) {
                                        dialog.show();
                                    }

                                    @Override
                                    public void onSucceed(int what, Response<String> response) {
                                        Log.d(TAG, "onSucceed: "+response.get());
                                        if (response.responseCode() == 200) {
                                            String str = response.get();
                                            try {
                                                JSONObject jObj=new JSONObject(str);
                                                String token = jObj.optJSONArray("faces").optJSONObject(0).optString("token");
                                                AppConfig.getInstance().putString("face_token",token);
                                                netWorkWarranty(data);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onFailed(int what, Response<String> response) {
                                        showToast(getString(R.string.request_failed));
                                    }

                                    @Override
                                    public void onFinish(int what) {
                                        dialog.dismiss();
                                    }
                                });

                            }
                        });
                    }
                }
            }
        }).start();
    }


    private void netWorkWarranty(final byte[] data) {
        final ProgressDialog dialog = new ProgressDialog(LivenessActivity.this, R.style.dialog);
        dialog.setMessage("请稍后");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(LivenessActivity.this);
                IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(
                        LivenessActivity.this);
                manager.registerLicenseManager(idCardLicenseManager);
                String uuid = "13213214321424";
                manager.takeLicenseFromNetwork(uuid);
                if (idCardLicenseManager.checkCachedLicense() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Intent intent = new Intent(LivenessActivity.this, IDCardScanActivity.class);
                            intent.putExtra("face_img",data);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(LivenessActivity.this, "联网授权失败！", Toast.LENGTH_SHORT).show();
                            startActivity(PIStartActivity.class,null);
                            finish();
                        }
                    });
                }

            }
        }).start();
    }

    /**
     * 活体检测失败 （DetectionListener的接口回调方法）
     */
    @Override
    public void onDetectionFailed(final DetectionFailedType type) {
        int resourceID = com.megvii.livenesslib.R.string.liveness_detection_failed;
        switch (type) {
            case ACTIONBLEND:
                resourceID = com.megvii.livenesslib.R.string.liveness_detection_failed_action_blend;
                break;
            case NOTVIDEO:
                resourceID = com.megvii.livenesslib.R.string.liveness_detection_failed_not_video;
                break;
            case TIMEOUT:
                resourceID = com.megvii.livenesslib.R.string.liveness_detection_failed_timeout;
                break;
        }
        dm.showFaceFailed(new DialogManager.Callback() {
            @Override
            public void handleLeft() {
                initDetecteSession();
            }

            @Override
            public void handleRight() {
                initDetecteSession();
                /*startActivity(PIStartActivity.class,null);
                finish();*/
            }
        });
    }

    /**
     * 活体验证中（这个方法会持续不断的回调，返回照片detection信息） （DetectionListener的接口回调方法）
     */
    @Override
    public void onFrameDetected(long timeout, DetectionFrame detectionFrame) {
        if (sensorUtil.isVertical()) {
            faceOcclusion(detectionFrame);
            handleNotPass(timeout);
            mFaceMask.setFaceInfo(detectionFrame);
        } else {
            if (sensorUtil.Y == 0)
                promptText.setText(com.megvii.livenesslib.R.string.meglive_getpermission_motion);
            else
                promptText.setText(com.megvii.livenesslib.R.string.meglive_phone_vertical);
        }
    }

    /**
     * 照镜子环节
     * 流程：1,先从返回的DetectionFrame中获取FaceInfo。在FaceInfo中可以先判断这张照片上的人脸是否有被遮挡的状况
     * ，入股有直接return
     * 2,如果没有遮挡就把SDK返回的DetectionFramed传入人脸质量检测管理类mFaceQualityManager中获取FaceQualityErrorType的list
     * 3.通过返回的list来判断这张照片上的人脸是否合格。
     * 如果返回list为空或list中FaceQualityErrorType的对象数量为0则表示这张照片合格开始进行活体检测
     */
    private void faceOcclusion(DetectionFrame detectionFrame) {
        mFailFrame++;
        if (detectionFrame != null) {
            FaceInfo faceInfo = detectionFrame.getFaceInfo();
            if (faceInfo != null) {
                if (faceInfo.eyeLeftOcclusion > 0.5 || faceInfo.eyeRightOcclusion > 0.5) {
                    if (mFailFrame > 10) {
                        mFailFrame = 0;
                        promptText.setText(com.megvii.livenesslib.R.string.meglive_keep_eyes_open);
                    }
                    return;
                }
                if (faceInfo.mouthOcclusion > 0.5) {
                    if (mFailFrame > 10) {
                        mFailFrame = 0;
                        promptText.setText(com.megvii.livenesslib.R.string.meglive_keep_mouth_open);
                    }
                    return;
                }
                boolean faceTooLarge = faceInfo.faceTooLarge;
                mIDetection.checkFaceTooLarge(faceTooLarge);
            }
        }
        // 从人脸质量检测管理类中获取错误类型list
        faceInfoChecker(mFaceQualityManager.feedFrame(detectionFrame));
    }

    private int mFailFrame = 0;

    public void faceInfoChecker(List<FaceQualityErrorType> errorTypeList) {
        if (errorTypeList == null || errorTypeList.size() == 0)
            handleStart();
        else {
            String infoStr = "";
            FaceQualityErrorType errorType = errorTypeList.get(0);
            if (errorType == FaceQualityErrorType.FACE_NOT_FOUND) {
                infoStr = getString(com.megvii.livenesslib.R.string.face_not_found);
            } else if (errorType == FaceQualityErrorType.FACE_POS_DEVIATED) {
                infoStr = getString(com.megvii.livenesslib.R.string.face_not_found);
            } else if (errorType == FaceQualityErrorType.FACE_NONINTEGRITY) {
                infoStr = getString(com.megvii.livenesslib.R.string.face_not_found);
            } else if (errorType == FaceQualityErrorType.FACE_TOO_DARK) {
                infoStr = getString(com.megvii.livenesslib.R.string.face_too_dark);
            } else if (errorType == FaceQualityErrorType.FACE_TOO_BRIGHT) {
                infoStr = getString(com.megvii.livenesslib.R.string.face_too_bright);
            } else if (errorType == FaceQualityErrorType.FACE_TOO_SMALL) {
                infoStr = getString(com.megvii.livenesslib.R.string.face_too_small);
            } else if (errorType == FaceQualityErrorType.FACE_TOO_LARGE) {
                infoStr = getString(com.megvii.livenesslib.R.string.face_too_large);
            } else if (errorType == FaceQualityErrorType.FACE_TOO_BLURRY) {
                infoStr = getString(com.megvii.livenesslib.R.string.face_too_blurry);
            } else if (errorType == FaceQualityErrorType.FACE_OUT_OF_RECT) {
                infoStr = getString(com.megvii.livenesslib.R.string.face_out_of_rect);
            }

            if (mFailFrame > 10) {
                mFailFrame = 0;
                promptText.setText(infoStr);
            }
        }
    }

    private int mCurStep = 0;// 检测动作的次数

    public void changeType(final Detector.DetectionType detectiontype, long timeout) {
        // 动画切换
        mIDetection.changeType(detectiontype, timeout);
        mFaceMask.setFaceInfo(null);

        // 语音播放
        if (mCurStep == 0) {
            mIMediaPlayer.doPlay(mIMediaPlayer.getSoundRes(detectiontype));
        } else {
            mIMediaPlayer.doPlay(com.megvii.livenesslib.R.raw.meglive_well_done);
            mIMediaPlayer.setOnCompletionListener(detectiontype);
        }
    }

    public void handleNotPass(final long remainTime) {
        if (remainTime > 0) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    timeOutText.setText(remainTime / 1000 + "");
                    mCircleProgressBar.setProgress((int) (remainTime / 100));
                }
            });
        }
    }

    private boolean mHasSurface = false;

    /**
     * TextureView启动成功后 启动相机预览和添加活体检测回调
     * （TextureView.SurfaceTextureListener的接口回调方法）
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mHasSurface = true;
        doPreview();

        // 添加活体检测回调 （本Activity继承了DetectionListener）
        mDetector.setDetectionListener(this);
        // 添加相机预览回调（本Activity继承了PreviewCallback）
        mICamera.actionDetect(this);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    /**
     * TextureView销毁后 （TextureView.SurfaceTextureListener的接口回调方法）
     */
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mHasSurface = false;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    private void doPreview() {
        if (!mHasSurface)
            return;

        mICamera.startPreview(camerapreview.getSurfaceTexture());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainHandler.removeCallbacksAndMessages(null);
        mICamera.closeCamera();
        mIMediaPlayer.close();

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDetector != null)
            mDetector.release();
        mDialogUtil.onDestory();
        mIDetection.onDestroy();
        sensorUtil.release();
    }
}