package com.ascba.rebate.activities.merchant;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.ascba.rebate.BuildConfig;
import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.MctModType;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.SellerDet;
import com.ascba.rebate.bean.SellerEntity;
import com.ascba.rebate.manager.LocationManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.FileUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SelectIconDialog;
import com.ascba.rebate.view.jd_selector.BottomDialog;
import com.ascba.rebate.view.jd_selector.City;
import com.ascba.rebate.view.jd_selector.County;
import com.ascba.rebate.view.jd_selector.OnAddressSelectedListener;
import com.ascba.rebate.view.jd_selector.Province;
import com.ascba.rebate.view.jd_selector.Street;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.RequestMethod;

import java.io.File;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by 李平 on 2017/11/30 15:50
 * Describe: 商家入驻
 */

public class MctEnterActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private ImageView imLogo, imDesign;
    private TextView tvName, tvType, tvTime, tvPhone, tvLocate, tvPLocate, tvAddress;
    private EditText etDesc;
    private Button btnApply;
    private File logoFile, designFile;
    private int type;//0 选择logo 1 选择店头
    private LocationManager lm;
    private int mStatus;
    private int errorStatus;
    private double lon;
    private double lat;
    private ScrollView scrollView;

    @Override
    protected int bindLayout() {
        return R.layout.activity_mct_enter;
    }

    public static void start(Activity activity, int mStatus) {
        Intent intent = new Intent(activity, MctEnterActivity.class);
        intent.putExtra("status", mStatus);
        activity.startActivity(intent);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        imLogo = fv(R.id.mct_logo);
        tvName = fv(R.id.mct_name);
        imDesign = fv(R.id.mct_design);
        tvType = fv(R.id.mct_type);
        tvTime = fv(R.id.mct_time);
        tvPhone = fv(R.id.mct_phone);
        tvLocate = fv(R.id.mct_locate);
        tvPLocate = fv(R.id.mct_proxy_locate);
        tvAddress = fv(R.id.mct_address);
        etDesc = fv(R.id.mct_desc);
        btnApply = fv(R.id.btn_apply);
        scrollView = fv(R.id.scrollView);

        fv(R.id.lat_mct_logo).setOnClickListener(this);
        fv(R.id.lat_mct_name).setOnClickListener(this);
        fv(R.id.lat_mct_design).setOnClickListener(this);
        fv(R.id.lat_mct_type).setOnClickListener(this);
        fv(R.id.lat_mct_time).setOnClickListener(this);
        fv(R.id.lat_mct_phone).setOnClickListener(this);
        fv(R.id.lat_mct_locate).setOnClickListener(this);
        fv(R.id.lat_mct_proxy_locate).setOnClickListener(this);
        fv(R.id.lat_mct_address).setOnClickListener(this);
        btnApply.setOnClickListener(this);
        getParams();

    }

    private void getParams() {
        Intent intent = getIntent();
        mStatus = intent.getIntExtra("status", 0);
        if (mStatus == 0) {//0商家未完善资料1商家已完善资料
            btnApply.setEnabled(true);
            btnApply.setText("提交");
        } else if (mStatus == 1) {
            btnApply.setEnabled(false);
            requestData();
        }
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.perfect, RequestMethod.GET, SellerDet.class);
        executeNetwork(0, "请稍后", request);
    }

    private void requestApply() {
        AbstractRequest request = buildRequest(UrlUtils.perfectPost, RequestMethod.POST, null);
        request.add("seller_name", tvName.getText().toString());
        request.add("seller_taglib", tvType.getText().toString());
        request.add("seller_business_hours", tvTime.getText().toString());
        request.add("seller_address", tvLocate.getText().toString());//
        request.add("region_name", tvPLocate.getText().toString());
        request.add("seller_localhost", tvAddress.getText().toString());//
        request.add("seller_tel", tvPhone.getText().toString());
        request.add("seller_description", etDesc.getText().toString());
        request.add("seller_lon", lon);
        request.add("seller_lat", lat);
        request.add("seller_image", new FileBinary(designFile));
        request.add("seller_cover_logo", new FileBinary(logoFile));

        executeNetwork(1, "请稍后", request);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "mStatus: "+mStatus+",errorStatus"+errorStatus);
        if (mStatus == 0 || errorStatus==0) {
            switch (v.getId()) {
                case R.id.lat_mct_logo://商家logo
                    type = 0;
                    showSelectIconDialog();
                    break;
                case R.id.lat_mct_name://店铺名称
                    MctModBaseActivity.start(this, new MctModType
                            (0, "店铺名称", getString(R.string.modify_name_hint), "请输入店铺名称", tvName.getText().toString(), CodeUtils.REQUEST_SHOP_NAME));
                    break;
                case R.id.lat_mct_design://店头形象
                    type = 1;
                    showSelectIconDialog();
                    break;
                case R.id.lat_mct_type://主营类目
                    MctTypeActivity.start(this, tvType.getText().toString());
                    break;
                case R.id.lat_mct_time://主营时间
                    MctModTimeActivity.start(this, tvTime.getText().toString());
                    break;
                case R.id.lat_mct_phone://联系电话
                    MctModBaseActivity.start(this, new MctModType
                            (1, "联系电话", getString(R.string.phone_hint), "请输入联系电话", tvPhone.getText().toString(), CodeUtils.REQUEST_SHOP_PHONE));
                    break;
                case R.id.lat_mct_locate://商家定位
                    startLocation();
                    break;
                case R.id.lat_mct_proxy_locate://选择地区
                    final BottomDialog dialog = new BottomDialog(this);
                    dialog.setOnAddressSelectedListener(new OnAddressSelectedListener() {
                        @Override
                        public void onAddressSelected(Province province, City city, County county, Street street) {
                            dialog.dismiss();
                            tvPLocate.setText(String.format("%s-%s-%s-%s", province.getName(), city.getName(), county.getName(), street != null ? street.getName() : ""));
                        }
                    });
                    dialog.show();
                    break;
                case R.id.lat_mct_address://详细地址
                    MctModBaseActivity.start(this, new MctModType
                            (2, "详细地址", getString(R.string.address_hint), "请输入详细地址", tvAddress.getText().toString(), CodeUtils.REQUEST_SHOP_ADDRESS));
                    break;
                case R.id.btn_apply:
                    if (allIsOk()) {
                        requestApply();
                    } else {
                        showToast("资料不完整");
                    }
                    break;
            }
        }
    }


    private void startLocation() {
        if (lm == null) {
            lm = new LocationManager(this, new LocationManager.LocateListener() {
                @Override
                public void onLocateSuccess(AMapLocation location) {
                    lon = location.getLongitude();
                    lat = location.getLatitude();
                    tvLocate.setText(location.getAddress());
                }

                @Override
                public boolean onLocateFailed(AMapLocation location) {
                    return false;
                }
            });
        }
    }

    private void showSelectIconDialog() {
        checkAndRequestAllPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA}, new PermissionCallback() {
            @Override
            public void requestPermissionAndBack(boolean isOk) {
                if (isOk) {
                    File file = FileUtils.getAppFile(MctEnterActivity.this, Environment.DIRECTORY_PICTURES);
                    logoFile = new File(file, "merchant_logo.jpg");
                    designFile = new File(file, "merchant_design.jpg");
                    SelectIconDialog dialog = new SelectIconDialog(MctEnterActivity.this,
                            type == 0 ? logoFile : designFile, R.style.dialog);
                    dialog.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CodeUtils.REQUEST_CAMERA_ICON:
                    Uri uri;
                    if (Build.VERSION.SDK_INT > 23) {
                        uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", type == 0 ? logoFile : designFile);
                    } else {
                        uri = Uri.fromFile(type == 0 ? logoFile : designFile);
                    }
                    cropImage(uri, Uri.fromFile(type == 0 ? logoFile : designFile));
                    break;
                case CodeUtils.REQUEST_ALBUM_ICON:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        Uri uri2;
                        if (Build.VERSION.SDK_INT > 23) {
                            uri2 = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", new File(picturePath));
                        } else {
                            uri2 = Uri.parse("file://" + picturePath);
                        }
                        cropImage(uri2, Uri.fromFile(type == 0 ? logoFile : designFile));
                    }
                    break;
                case CodeUtils.REQUEST_CROP:
                    Luban.with(this).load(type == 0 ? logoFile : designFile)
                            .ignoreBy(350)
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                    if (type == 0) {
                                        logoFile = file;
                                        imLogo.setImageBitmap(bitmap);
                                    } else {
                                        designFile = file;
                                        imDesign.setImageBitmap(bitmap);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                }
                            }).launch();
                    break;
                case CodeUtils.REQUEST_SHOP_NAME:
                    tvName.setText(data.getStringExtra("value"));
                    break;
                case CodeUtils.REQUEST_SHOP_PHONE:
                    tvPhone.setText(data.getStringExtra("value"));
                    break;
                case CodeUtils.REQUEST_SHOP_ADDRESS:
                    tvAddress.setText(data.getStringExtra("value"));
                    break;
                case CodeUtils.REQUEST_SHOP_TIME:
                    tvTime.setText(data.getStringExtra("business_data_time"));
                    break;
                case CodeUtils.REQUEST_MCT_TYPE:
                    tvType.setText(data.getStringExtra("type"));
                    break;
            }
        }
    }

    //裁剪图片
    private void cropImage(Uri inUri, Uri outUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inUri, "image/*");
        //是否裁剪
        intent.putExtra("crop", "true");
        //设置xy的裁剪比例
        //intent.putExtra("aspectX", 1);
        //intent.putExtra("aspectY", 1);
        //设置输出的宽高
        //intent.putExtra("outputX", 720);
        //intent.putExtra("outputY", 720);
        intent.putExtra("scale", true);//去除黑边
        intent.putExtra("scaleUpIfNeeded", true);//去除黑边
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //输入图片的Uri，指定以后，可以在这个uri获得图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        //是否返回图片数据，可以不用，直接用uri就可以了
        intent.putExtra("return-data", false);
        //设置输入图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //是否关闭面部识别
        intent.putExtra("noFaceDetection", true); // no face detection
        //启动
        startActivityForResult(intent, CodeUtils.REQUEST_CROP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != lm) {
            lm.destroyLocation();
        }
    }

    private boolean allIsOk() {
        return logoFile != null && logoFile.exists() && designFile != null && designFile.exists()
                && !TextUtils.isEmpty(tvName.getText().toString()) && !TextUtils.isEmpty(tvType.getText().toString())
                && !TextUtils.isEmpty(tvTime.getText().toString()) && !TextUtils.isEmpty(tvPhone.getText().toString())
                && !TextUtils.isEmpty(tvLocate.getText().toString()) && !TextUtils.isEmpty(tvPLocate.getText().toString())
                && !TextUtils.isEmpty(tvAddress.getText().toString()) && !TextUtils.isEmpty(etDesc.getText().toString());

    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {//获取资料
            SellerDet sellerDet = (SellerDet) result.getData();
            setUI(sellerDet);
        } else if (what == 1) {//提交资料
            showToast(result.getMsg());
            requestData();
        }
    }

    private void setUI(SellerDet sellerDet) {
        if (sellerDet != null) {
            errorStatus = sellerDet.getSeller_error_status();
            btnApply.setEnabled(errorStatus==0);
            btnApply.setText(sellerDet.getSeller_error_status_text());
            SellerDet.SellerBean seller = sellerDet.getSeller();
            mStatus = seller.getSeller_status();
            Picasso.with(this).load(seller.getSeller_cover_logo()).placeholder(R.mipmap.module_loading).into(imLogo);
            tvName.setText(seller.getSeller_name());
            Picasso.with(this).load(seller.getSeller_image()).placeholder(R.mipmap.gift_head_loading).into(imDesign);
            tvType.setText(seller.getSeller_taglib());
            tvTime.setText(seller.getSeller_business_hours());
            tvPhone.setText(seller.getSeller_tel());
            tvLocate.setText(seller.getSeller_address());
            tvPLocate.setText(seller.getRegion_name());
            tvAddress.setText(seller.getSeller_localhost());
            etDesc.setText(seller.getSeller_description());
            if(errorStatus == 1){
                etDesc.clearFocus();
                etDesc.setFocusable(false);
            }

        }
    }
}
