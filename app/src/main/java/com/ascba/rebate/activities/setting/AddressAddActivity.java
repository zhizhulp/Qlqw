package com.ascba.rebate.activities.setting;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.AddressEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.RegexUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ClearEditText;
import com.suke.widget.SwitchButton;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;

import cn.qqtheme.framework.AddressPickerView;
import cn.qqtheme.framework.InitAddressTask;
import cn.qqtheme.framework.beans.Province;

/**
 * Created by Jero on 2017/9/19 0019.
 */

public class AddressAddActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private static final int Contacts = 449;
    public static final int ADD_TYPE = 149;
    public static final int EDIT_TYPE = 385;

    private int mType = ADD_TYPE;
    private AddressEntity editAddress;

    private ImageView imContacts;
    private ClearEditText cetName, cetMobile;
    private RelativeLayout rlAddress;
    private TextView tvAddress;
    private EditText etDetail;
    private SwitchButton switchButton;

    private AddressPickerView pickerView;
    private ProgressDialog proDialog;
    private Province province;
    private Province.City city;
    private Province.City.District district;

    @Override
    protected int bindLayout() {
        return R.layout.activity_address_add;
    }

    @Override
    protected void initViews(final Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                save();
            }
        });
        imContacts = fv(R.id.address_contacts_iv);
        cetName = fv(R.id.address_add_name_et);
        cetMobile = fv(R.id.address_add_mobile_et);
        rlAddress = fv(R.id.address_select_rl);
        tvAddress = fv(R.id.address_add_tv);
        etDetail = fv(R.id.address_add_detail);
        imContacts.setOnClickListener(this);
        rlAddress.setOnClickListener(this);
        switchButton = fv(R.id.fingerprint_switch);

        initType();
        initRegion();
    }

    private void initType() {
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", ADD_TYPE);
        if (mType == EDIT_TYPE) {//切换编辑模式
            editAddress = JSON.parseObject(getIntent().getStringExtra("address"), AddressEntity.class);
            cetName.setText(editAddress.getConsignee());
            cetMobile.setText(editAddress.getMobile());
            tvAddress.setText(editAddress.getAddress_region());
            etDetail.setText(editAddress.getAddress());
            switchButton.setChecked(editAddress.getType() == 1 ? true : false);
            if (switchButton.isChecked()) {
                switchButton.setEnabled(false);
            }
        }
    }

    private void save() {
        if (TextUtils.isEmpty(cetName.getText().toString())) {
            showToast("请填写收货人");
            return;
        }
        if (!RegexUtils.isUserName(cetName.getText().toString())) {
            showToast("收货人不能输入特殊字符");
            return;
        }
        if (TextUtils.isEmpty(cetMobile.getText().toString())) {
            showToast("请输入联系电话");
            return;
        }
        if (!RegexUtils.isMobilePhoneNum(cetMobile.getText().toString())) {
            showToast("手机号码格式不正确请重新输入");
            return;
        }
        if (province == null || province.getId() == 0 ||
                city == null || city.getId() == 0 ||
                district == null || district.getId() == 0) {
            showToast("请选择所在地区");
            return;
        }
        if (TextUtils.isEmpty(etDetail.getText().toString())) {
            showToast("请填写详细地址");
            return;
        }
        if (!RegexUtils.isUserName(etDetail.getText().toString())) {
            showToast("详细地址不能输入特殊字符");
            return;
        }
        if (etDetail.getText().toString().length() < 5) {
            showToast("详细地址不能少于5个字");
            return;
        }
        if (mType == ADD_TYPE) {
            requestAddressAddNetwork(ADD_TYPE);
        } else if (mType == EDIT_TYPE) {
            requestAddressEditNetwork(EDIT_TYPE);
        }
    }

    public void requestAddressAddNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.addressAdd, RequestMethod.POST, null);
        request.add("consignee", cetName.getText().toString());
        request.add("mobile", cetMobile.getText().toString());
        request.add("province", province.getId());
        request.add("city", city.getId());
        request.add("district", district.getId());
        request.add("address", etDetail.getText().toString());
        request.add("type", switchButton.isChecked() ? 1 : 0);
        executeNetwork(what, "请稍后", request);
    }

    public void requestAddressEditNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.addressEdit, RequestMethod.POST, null);
        request.add("member_address_id", editAddress.getAddress_id());
        request.add("consignee", cetName.getText().toString());
        request.add("mobile", cetMobile.getText().toString());
        request.add("province", province.getId());
        request.add("city", city.getId());
        request.add("district", district.getId());
        request.add("address", etDetail.getText().toString());
        request.add("type", switchButton.isChecked() ? 1 : 0);
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        switch (what) {
            case ADD_TYPE:
//                String data = (String) result.getData();
//                JSONObject dataObj = JSON.parseObject(data);
//                String info = dataObj.getString("info");
                AddressEntity address = JSON.parseObject(JSON.parseObject(result.getData().toString()).getString("info"), AddressEntity.class);
                //region 补全地址全部信息，以便地址列表修改
                address.setProvince(province.getId());
                address.setCity(city.getId());
                address.setDistrict(district.getId());
                address.setAddress(etDetail.getText().toString());
                address.setAddress_region(tvAddress.getText().toString());
                address.setType(switchButton.isChecked() ? 1 : 0);
                //endregion
                Intent intent = getIntent();
                intent.putExtra("address", address);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case EDIT_TYPE:
                setResult(RESULT_OK);
                showToast("编辑地址成功");
                finish();
                break;
        }

    }

    /*
         初始化地区数据
        */
    private void initRegion() {
        InitAddressTask task = new InitAddressTask(this);
        if (mType == EDIT_TYPE) {
            task.setRegionId(editAddress.getProvince(), editAddress.getCity(), editAddress.getDistrict());
        }
        task.setInitData(new InitAddressTask.InitData() {
            @Override
            public void onSuccess(ArrayList<Province> data, Province argo, Province.City arg1, Province.City.District arg2) {
                pickerView = new AddressPickerView(AddressAddActivity.this, data);
                if (argo == null) {
                    //默认地区
                    pickerView.setRegion("北京市", "北京市", "东城区");
                } else {
                    tvAddress.setText(argo.getName() + " " + arg1.getName() + " " + arg2.getName());
                    pickerView.setRegion(argo.getName(), arg1.getName(), arg2.getName());
                    if (mType == EDIT_TYPE) {
                        province = argo;
                        city = arg1;
                        district = arg2;
                    }
                }
                pickerView.setCallback(new InitAddressTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {
                        showToast("数据初始化失败");
                    }

                    @Override
                    public void onAddressPicked(Province argo, Province.City arg1, Province.City.District arg2) {
                        province = argo;
                        city = arg1;
                        district = arg2;
                        pickerView.setRegion(argo.getName(), arg1.getName(), arg2.getName());
                        tvAddress.setText(province.getName() + " " + city.getName() + " " + district.getName());
                    }
                });

                if (proDialog != null && proDialog.isShowing()) {
                    proDialog.dismiss();
                    pickerView.showPicker();
                }
            }

            @Override
            public void onFailed() {
                if (proDialog != null && proDialog.isShowing()) {
                    proDialog.dismiss();
                }
                showToast("数据初始化失败");
            }
        });
        task.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_select_rl:
                if (pickerView == null) {
                    proDialog = ProgressDialog.show(this, null, "正在初始化数据...", true, true);
                } else {
                    pickerView.showPicker();
                }
                break;
            case R.id.address_contacts_iv:
                checkAndRequestAllPermission(new String[]{Manifest.permission.READ_CONTACTS}, new PermissionCallback() {
                    @Override
                    public void requestPermissionAndBack(boolean isOk) {
                        if (isOk) {
                            startActivityForResult(new Intent(Intent.ACTION_PICK,
                                    ContactsContract.Contacts.CONTENT_URI), Contacts);
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Contacts && data != null) {
                Uri uri = data.getData();
                try {
                    String[] contacts = getPhoneContacts(uri);
                    if (contacts != null) {
                        cetName.setText(TextUtils.isEmpty(contacts[0]) ? "" : contacts[0]);
                        cetMobile.setText(getNumbers(contacts[1].trim()));
                    }
                } catch (NullPointerException e) {
                    showToast("联系人没有电话号码");
                }
            }
        }
    }

    /**
     * 获取联系人数据
     */
    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            cursor.close();
            Cursor phone;
            try {
                phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            } catch (Exception e) {
                return null;
            }
            if (phone != null && phone.moveToFirst()) {
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone.close();
            }
        }
        return contact;
    }

    //截取数字
    public String getNumbers(String content) {
        String str2 = "";
        if (content != null && !"".equals(content)) {
            for (int i = 0; i < content.length(); i++) {
                if (content.charAt(i) >= 48 && content.charAt(i) <= 57) {
                    str2 += content.charAt(i);
                }
            }
        }
        return str2;
    }
}
