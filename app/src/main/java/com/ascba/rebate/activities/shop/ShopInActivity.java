package com.ascba.rebate.activities.shop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ascba.rebate.BuildConfig;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.company_identification.ComMsgActivity;
import com.ascba.rebate.activities.company_identification.InPutCNActivity;
import com.ascba.rebate.activities.merchant.MctModBaseActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.ComMsg;
import com.ascba.rebate.bean.MctModType;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.ShopDet;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.FileUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SelectIconDialog;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

import java.io.File;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ShopInActivity extends BaseDefaultNetActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, OnCompressListener {

    private EditText editText;
    private RadioGroup radioGroup;
    private TextView tvName;
    private ImageView imHead;
    private TextView tvType;
    private TextView tvPhone;
    private RadioButton radioPerson;
    private RadioButton radioCompany;
    private Button btnApply;
    private File logo;
    private ShopDet shopDet;
    private boolean isFirst;
    private int typeId = -1;
    private View imTypeGo;

    @Override
    protected int bindLayout() {
        return R.layout.activity_shop_in;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        radioGroup = fv(R.id.radioGroup);
        radioPerson = fv(R.id.radio_person);
        radioCompany = fv(R.id.radio_company);
        radioGroup.setOnCheckedChangeListener(this);
        tvName = fv(R.id.tv_name);
        imHead = fv(R.id.im_head);
        tvType = fv(R.id.tv_type);
        tvPhone = fv(R.id.tv_phone);
        editText = fv(R.id.editTextHint);
        btnApply = fv(R.id.btn_apply);
        imTypeGo = fv(R.id.imageView6);

        btnApply.setOnClickListener(this);
        fv(R.id.lat_name).setOnClickListener(this);
        fv(R.id.lat_head).setOnClickListener(this);
        fv(R.id.lat_type).setOnClickListener(this);
        fv(R.id.tv_how).setOnClickListener(this);
        fv(R.id.lat_phone).setOnClickListener(this);

        requestData();
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.storePerfect, RequestMethod.GET, ShopDet.class);
        executeNetwork(0, "请稍后", request);
    }

    private void requestApply() {
        AbstractRequest request = buildRequest(UrlUtils.storePerfectPost, RequestMethod.POST, null);
        request.add("store_name", tvName.getText().toString());
        request.add("store_type", radioPerson.isChecked() ? 1 : 2);
        request.add("store_telphone", tvPhone.getText().toString());
        request.add("store_description", editText.getText().toString());
        if (logo != null && logo.exists())
            request.add("store_logo", logo);
        request.add("primary_class_key", typeId);
        executeNetwork(1, "请稍后", request);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (shopDet != null && shopDet.getStore_type() == 1) {
            if (checkedId == R.id.radio_company) {
                int status = shopDet.getCompany_status();
                String statusText = shopDet.getCompany_status_text();
                if (status == 0) {
                    dm.showAlertDialog2(statusText, "取消", "确定", new DialogManager.Callback() {
                        @Override
                        public void handleRight() {
                            radioPerson.setChecked(true);
                            startActivity(InPutCNActivity.class, null);
                        }

                        @Override
                        public void handleLeft() {
                            radioPerson.setChecked(true);
                        }
                    });
                } else if (status == 1) {
                    dm.showAlertDialog(statusText, "我知道了", new DialogManager.Callback() {
                        @Override
                        public void handleLeft() {
                            radioPerson.setChecked(true);
                        }
                    });
                } else if (status == 2) {
                    dm.showAlertDialog2(statusText, "取消", "确定", new DialogManager.Callback() {
                        @Override
                        public void handleRight() {
                            radioPerson.setChecked(true);
                            findCompanyInfo();
                        }

                        @Override
                        public void handleLeft() {
                            radioPerson.setChecked(true);
                        }
                    });
                }
            }
        }
    }

    private void findCompanyInfo() {
        AbstractRequest request = buildRequest(UrlUtils.getCompanyInfo, RequestMethod.GET, ComMsg.class);
        executeNetwork(2, "请稍后", request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lat_name://店铺名称
                MctModBaseActivity.start(this, new MctModType
                        (0, "店铺名称", getString(R.string.modify_name_hint), "请输入店铺名称",
                                tvName.getText().toString(), CodeUtils.REQUEST_SHOP_NAME));
                break;
            case R.id.lat_head://店铺形象
                checkAndRequestAllPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, new PermissionCallback() {
                    @Override
                    public void requestPermissionAndBack(boolean isOk) {
                        if (isOk) {
                            if (logo == null) {
                                logo = new File(FileUtils.getAppFile(MyApplication.getInstance(),
                                        Environment.DIRECTORY_PICTURES), "qlqw201801251423shop.png");
                            }
                            SelectIconDialog dialog = new SelectIconDialog(ShopInActivity.this, logo, R.style.dialog);
                            dialog.show();
                        }
                    }
                });
                break;
            case R.id.lat_type://经营类目 写入后不可更改
                if (shopDet.getPrimary_class_status() == 0) return;
                OnLineTypeActivity.start(this, tvType.getText().toString());
                break;
            case R.id.tv_how://如何选择类目
                WebViewBaseActivity.start(this, shopDet.getStore_class_h5_title(), shopDet.getStore_class_h5());
                break;
            case R.id.lat_phone://电话
                MctModBaseActivity.start(this, new MctModType
                        (1, "联系电话", getString(R.string.phone_hint), "请输入联系电话",
                                tvPhone.getText().toString(), CodeUtils.REQUEST_SHOP_PHONE));
                break;
            case R.id.btn_apply://提交资料
                if (isFirst) {
                    if (checkParams()) {
                        requestApply();
                    }
                } else {
                    requestApply();
                }
                break;
        }
    }

    private boolean checkParams() {
        if (TextUtils.isEmpty(tvName.getText().toString())) {
            showToast("请填写店铺名称");
            return false;
        }
        if (logo == null || !logo.exists()) {
            showToast("请上传店铺形象");
            return false;
        }
        if (TextUtils.isEmpty(tvType.getText().toString())) {
            showToast("请填写主营类目");
            return false;
        }
        if (TextUtils.isEmpty(tvPhone.getText().toString())) {
            showToast("请填写联系电话");
            return false;
        }
        if (TextUtils.isEmpty(editText.getText().toString())) {
            showToast("请填写店铺介绍");
            return false;
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            shopDet = (ShopDet) result.getData();
            radioPerson.setChecked(shopDet.getStore_type() == 1);
            radioCompany.setChecked(shopDet.getStore_type() == 2);
            int canChange = shopDet.getType_can_change();
            radioPerson.setEnabled(canChange != 2);
            radioCompany.setEnabled(canChange != 2);
            tvName.setText(shopDet.getStore_name());
            String storeLogo = shopDet.getStore_logo();
            if (!TextUtils.isEmpty(storeLogo))
                Picasso.with(this).load(storeLogo).
                        placeholder(R.mipmap.shop_placeholder).into(imHead);
            String classValue = shopDet.getPrimary_class_value();
            typeId = shopDet.getPrimary_class_key();
            isFirst = TextUtils.isEmpty(classValue);
            tvType.setText(classValue);
            tvPhone.setText(shopDet.getStore_telphone());
            editText.setText(shopDet.getStore_description());
            imTypeGo.setVisibility(shopDet.getPrimary_class_status() == 0 ? View.GONE : View.VISIBLE);
        } else if (what == 1) {
            startActivity(new Intent(this, ShopEnterActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("type", 2));
        } else if (what == 2) {
            ComMsg comMsg = (ComMsg) result.getData();
            Bundle b = new Bundle();
            b.putParcelable("company_msg", comMsg);
            AppConfig.getInstance().putInt("company_status", comMsg.getStatus());
            startActivity(ComMsgActivity.class, b);
        }
    }

    @Override
    protected void mHandle404(int what, Result result) {
        super.mHandle404(what, result);
        if (what == 0) {
            finish();
        }
    }

    @Override
    protected void mHandleFailed(int what) {
        super.mHandleFailed(what);
        if (what == 0) {
            finish();
        }
    }

    @Override
    protected void mHandleReLogin(int what, Result result) {
        super.mHandleReLogin(what, result);
        if (what == 0) {
            finish();
        }
    }

    @Override
    protected void mHandleNoNetwork(int what) {
        super.mHandleNoNetwork(what);
        if (what == 0) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CodeUtils.REQUEST_SHOP_NAME) {
                tvName.setText(data.getStringExtra("value"));
            } else if (requestCode == CodeUtils.REQUEST_SHOP_PHONE) {
                tvPhone.setText(data.getStringExtra("value"));
            } else if (requestCode == CodeUtils.REQUEST_CAMERA_ICON) {
                Uri uri;
                if (Build.VERSION.SDK_INT > 23) {
                    uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", logo);
                } else {
                    uri = Uri.fromFile(logo);
                }
                cropImage(uri, Uri.fromFile(logo));
            } else if (requestCode == CodeUtils.REQUEST_ALBUM_ICON) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Uri uri;
                    if (Build.VERSION.SDK_INT > 23) {
                        uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", new File(picturePath));
                    } else {
                        uri = Uri.parse("file://" + picturePath);
                    }
                    cropImage(uri, Uri.fromFile(logo));
                }
            } else if (requestCode == CodeUtils.REQUEST_MCT_TYPE) {
                typeId = data.getIntExtra("type_id", -1);
                tvType.setText(data.getStringExtra("type"));
            } else if (requestCode == CodeUtils.REQUEST_CROP) {
                Luban.with(this).load(logo)
                        .ignoreBy(350)
                        .setCompressListener(this).launch();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSuccess(File file) {
        logo = file;
        imHead.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
    }

    @Override
    public void onError(Throwable e) {

    }

    //裁剪图片
    private void cropImage(Uri inUri, Uri outUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inUri, "image/*");
        //是否裁剪
        intent.putExtra("crop", "true");
        //设置xy的裁剪比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //设置输出的宽高
        intent.putExtra("outputX", 720);
        intent.putExtra("outputY", 720);
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
}
