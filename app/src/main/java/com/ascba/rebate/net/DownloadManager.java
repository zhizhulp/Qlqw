package com.ascba.rebate.net;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.BuildConfig;
import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.utils.FileUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 李平 on 2017/11/8 18:07
 * Describe:app下载管理
 */

public class DownloadManager {
    private static final int CANCEL_SIGN = 50;
    private String TAG = getClass().getSimpleName();
    private String url;//下载链接
    private String msg;//更新内容
    private Context context;
    private NotificationManager manager;
    private Notification notification;
    private Notification.Builder builder;
    private boolean force;//是否强制更新
    private ProgressDialog progressDialog;
    private int contentLen;
    private int currentToatalLen;
    private static final int PROGRESS_MAX = 0X1;
    private static final int UPDATE = 0X2;

    public DownloadManager(Context context, boolean force) {
        this.context = context;
        this.force = force;
    }

    public void requestAndDown() {
        StringRequest request = new StringRequest(UrlUtils.getUpgrade, RequestMethod.GET);
        CallServer.getInstance().addStringRequest(-8, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                Log.d(TAG, "parseResponse: " + response.get());
                String data = response.get();
                JSONObject jObj = JSON.parseObject(data);
                int code = jObj.getIntValue("code");
                if (code == 200) {
                    Result.VersionUpgrade update = jObj.getObject("data", Result.VersionUpgrade.class);
                    if (update != null) {
                        String versionUrl = update.getVersion_url();
                        if (versionUrl != null) {
                            url = versionUrl;
                        }
                        String msg = update.getVersion_remarks();
                        if (msg != null) {
                            DownloadManager.this.msg = msg;
                        }
                        if (force) {
                            showDownloadDialogForce();
                        } else {
                            showDownloadDialog();
                        }
                    }
                }

            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });

    }

    //不强制升级
    public void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("更新通知");
        builder.setMessage(msg == null ? "您有新的更新，点击确定下载" : msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (context instanceof BaseUIActivity) {
                    ((BaseUIActivity) context).checkAndRequestAllPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new BaseUIActivity.PermissionCallback() {
                        @Override
                        public void requestPermissionAndBack(boolean isOk) {
                            if (isOk) {
                                downloadApp();
                            }
                        }
                    });
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        AppConfig.getInstance().putBoolean("need_show_update", false);
    }

    //强制升级
    public void showDownloadDialogForce() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("更新通知");
        builder.setMessage(msg == null ? "您有新的更新，点击确定下载" : msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (context instanceof BaseUIActivity) {
                    ((BaseUIActivity) context).checkAndRequestAllPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new BaseUIActivity.PermissionCallback() {
                        @Override
                        public void requestPermissionAndBack(boolean isOk) {
                            if (isOk) {
                                downloadApp();
                            } else {
                                ((BaseUIActivity) context).finish();
                            }
                        }
                    });
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void showNotification() {
        builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.logo);  //设置通知的图标
        notification = builder.getNotification();
        manager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_NO_CLEAR;
        downloadApp();
    }

    public void downloadApp() {
//        DownloadRequest request = new DownloadRequest(url, RequestMethod.GET,
//                FileUtils.getAppFile(context, Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), "app_release_qlqw.apk", true, true);
//        request.setCancelSign(CANCEL_SIGN);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //CallServer.getInstance().cancelDownloadBySign(CANCEL_SIGN);
        //CallServer.getInstance().download(5, request, downloadListener);
        new DownLoad().execute(url);
    }

    private void checkFilePath(String filePath) {
        Log.d(TAG, "checkFilePath: " + filePath);
        if (!TextUtils.isEmpty(filePath)) {
            if (filePath.endsWith(".apk")) {
                installFile(filePath);
            } else {
                Log.d(TAG, "installApp: unavailable file");
            }

        } else {
            Log.e(TAG, "installApp: empty file");
        }
    }

    private void installFile(String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT > 23) {
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File(filePath));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setDataAndType(uri, "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(i);
        } else {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            context.startActivity(i);
        }
    }

    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onDownloadError(int what, Exception exception) {
            Log.d(TAG, "onDownloadError: " + exception.getMessage());
        }

        @Override
        public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
            Log.d(TAG, "onStart: ");
            if (progressDialog != null) {
                progressDialog.show();
            }
        }

        @Override
        public void onProgress(int what, int progress, long fileCount, long speed) {
            Log.d(TAG, "onProgress: " + progress);
            if (progressDialog != null) {
                progressDialog.setProgress(progress);
            }
        }

        @Override
        public void onFinish(int what, String filePath) {
            Log.d(TAG, "onFinish: " + filePath);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            checkFilePath(filePath);
        }

        @Override
        public void onCancel(int what) {
            Log.d(TAG, "onCancel: ");
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    };

    @SuppressLint("StaticFieldLeak")
    class DownLoad extends AsyncTask<String, Integer, String> {
        //在执行实际的后台操作前被UI Thread调用
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute: ");
            super.onPreExecute();
            //准备下载前的初始进度
            progressDialog.show();
            progressDialog.setProgress(0);
        }

        //在onPreExecute执行，该方法运行在后台线程中
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                //获取连接
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //获取下载文件的大小
                contentLen = connection.getContentLength();
                //根据下载文件大小设置进度条最大值（使用标记区别实时进度更新）
                publishProgress(PROGRESS_MAX, contentLen);
                //循环下载（边读取边存入）
                BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new
                        File(FileUtils.getAppFile(context, Environment.DIRECTORY_DOWNLOADS) + "/app-release_qlqw.apk")));
                int len;
                byte[] bytes = new byte[1024];
                while ((len = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);
                    bos.flush();
                    //实时更新下载进度（使用标记区别最大值）
                    publishProgress(UPDATE, len);
                }
                bos.close();
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "下载完成";
        }

        //在publishProgress被调用后，UI thread会调用这个方法
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            switch (values[0]) {
                case PROGRESS_MAX:
                    progressDialog.setMax(100);
                    break;
                case UPDATE:
                    currentToatalLen +=values[1];
                    Log.d(TAG, "onProgressUpdate: " + currentToatalLen * 100 / contentLen);
                    progressDialog.setProgress(currentToatalLen * 100 / contentLen);
                    break;
            }
        }

        //doInBackground方法执行完后被UI thread执行
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            checkFilePath(FileUtils.getAppFile(context, Environment.DIRECTORY_DOWNLOADS) + "/app-release_qlqw.apk");
        }
    }
}

