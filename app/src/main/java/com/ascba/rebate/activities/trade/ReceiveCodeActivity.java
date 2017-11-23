package com.ascba.rebate.activities.trade;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.BillActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.BitmapUtils;
import com.ascba.rebate.utils.FileUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by 李平 on 2017/10/12 10:28
 * Describe:二维码收款界面
 */

public class ReceiveCodeActivity extends BaseDefaultNetActivity {

    private ImageView imCode;
    private Bitmap bitmap;

    @Override
    protected int bindLayout() {
        return R.layout.activity_receive_code;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        imCode = fv(R.id.im_code);
        imCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveBitmap();
                return true;
            }
        });
        fv(R.id.lat_see_records).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("mine_type",3);
                startActivity(BillActivity.class, bundle);
            }
        });
        mStatusView.empty();
        requestSever();
    }

    private void saveBitmap() {
        checkAndRequestAllPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionCallback() {
            @Override
            public void requestPermissionAndBack(boolean isOk) {
                if (isOk) {
                    File rootDir = FileUtils.getRootPath();
                    File fileWithName = new File(rootDir, "qlqw_seller_code_20171019.png");
                    try {
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileWithName));
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        bos.flush();
                        bos.close();
                        showToast("保存成功");
                    } catch (IOException e) {
                        e.printStackTrace();
                        showToast("保存失败");
                    }

                }
            }
        });
    }

    @Override
    protected boolean hasCache() {
        return true;
    }

    private void requestSever() {
        AbstractRequest request = buildRequest(UrlUtils.receivable_url, RequestMethod.GET, null);
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            mStatusView.content();
            String data = (String) result.getData();
            JSONObject dataObj = JSON.parseObject(data);
            final String codeMsg = dataObj.getString("url");
            setImageCode(codeMsg);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void setImageCode(final String codeMsg) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(codeMsg, (int) ScreenDpiUtils.dp2px(ReceiveCodeActivity.this, 185),
                        Color.BLACK, Color.WHITE, null);
            }

            @Override
            protected void onPostExecute(final Bitmap bitmap) {
                if (bitmap != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getBitmap(bitmap);
                        }
                    }).start();
                } else {
                    showToast("二维码生成失败！");
                }
            }
        }.execute();
    }

    private void getBitmap(final Bitmap bitmap) {
        String avatar = AppConfig.getInstance().getString("avatar", null);
        try {
            URL url = new URL(avatar);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Bitmap logo ;
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                logo = BitmapFactory.decodeStream(inputStream);
            } else {
                logo = ((BitmapDrawable) ContextCompat.getDrawable(this, R.mipmap.head_loading)).getBitmap();
            }
            final Bitmap finalLogo = BitmapUtils.getRoundBitmap(logo, 5,5);
            imCode.post(new Runnable() {
                @Override
                public void run() {
                    ReceiveCodeActivity.this.bitmap = BitmapUtils.addLogoToQRCode(bitmap, finalLogo);
                    imCode.setImageBitmap(ReceiveCodeActivity.this.bitmap);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            imCode.post(new Runnable() {
                @Override
                public void run() {
                    imCode.setImageBitmap(bitmap);
                }
            });
        }
    }
}
