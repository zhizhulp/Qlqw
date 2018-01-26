package com.ascba.rebate.activities.shop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.company_identification.ComMsgActivity;
import com.ascba.rebate.activities.company_identification.InPutCNActivity;
import com.ascba.rebate.activities.merchant.MctModBaseActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
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
    private boolean isFirst;

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

        btnApply.setOnClickListener(this);
        fv(R.id.lat_name).setOnClickListener(this);
        fv(R.id.lat_head).setOnClickListener(this);
        fv(R.id.lat_type).setOnClickListener(this);
        fv(R.id.tv_how).setOnClickListener(this);
        fv(R.id.lat_phone).setOnClickListener(this);

        setParams();
    }

    private void setParams() {
        isFirst = getIntent().getBooleanExtra("is_first", true);
        if (!isFirst) {
            requestData();
        } else {
            radioPerson.setChecked(true);
        }
    }

    private void start(Context context, boolean isFirst) {
        Intent intent = new Intent(context, ShopInActivity.class);
        intent.putExtra("is_first", isFirst);
        startActivity(intent);
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
        request.add("store_logo", logo);
        request.add("primary_class_key", tvType.getText().toString());
        executeNetwork(1, "请稍后", request);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.d(TAG, "onCheckedChanged: " + checkedId);
        if (checkedId == R.id.radio_company) {
            int status = AppConfig.getInstance().getInt("company_status", 0);
            if (status == 0) {
                dm.showAlertDialog2("未审核", "取消", "确定", new DialogManager.Callback() {
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
                dm.showAlertDialog("审核中", "我知道了", new DialogManager.Callback() {
                    @Override
                    public void handleLeft() {
                        radioPerson.setChecked(true);
                    }
                });
            } else if (status == 2) {
                dm.showAlertDialog2("审核资料有误", "取消", "确定", new DialogManager.Callback() {
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
                break;
            case R.id.tv_how://如何选择类目
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
            showToast("请上传店头形象");
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
            ShopDet shopDet = (ShopDet) result.getData();
            radioPerson.setChecked(shopDet.getStore_type() == 1);
            radioCompany.setChecked(shopDet.getStore_type() == 2);
            tvName.setText(shopDet.getStore_name());
            Picasso.with(this).load(shopDet.getStore_logo()).
                    placeholder(R.mipmap.shop_placeholder).into(imHead);
            tvType.setText(shopDet.getStore_type() + "");
            tvPhone.setText(shopDet.getStore_telphone());
            editText.setEnabled(false);
            editText.setText(shopDet.getStore_description());
        } else if (what == 1) {
            showToast(result.getMsg());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CodeUtils.REQUEST_SHOP_NAME) {
                tvName.setText(data.getStringExtra("value"));
            } else if (requestCode == CodeUtils.REQUEST_SHOP_PHONE) {
                tvPhone.setText(data.getStringExtra("value"));
            } else if (requestCode == CodeUtils.REQUEST_CAMERA_ICON) {
                if (logo != null && logo.exists()) {
                    handleImage();
                }
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
                    logo = new File(picturePath);
                    handleImage();
                }
            }
        }
    }

    private void handleImage() {
        Luban.with(this).load(logo).setCompressListener(this).launch();
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
}
