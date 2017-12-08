package com.ascba.rebate.activities.seller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.success.TextInfoSuccessActivity;
import com.ascba.rebate.activities.setting.AddressActivity;
import com.ascba.rebate.activities.setting.AddressAddActivity;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.AddressEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by Jero on 2017/11/25 0025.
 */

public class SellerInvoiceActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private int type = 1;//  1 个人 2 公司

    private EditText titleEt, numEt, remarkEt;
    private TextView contextTv, moneyTv, nameTv, mobileTv, addressTv, freightTv, typeTv;
    private Button btn;
    private LinearLayout addressTopLat, companyLat;

    private String data;
    private AddressEntity address;

    @Override
    protected int bindLayout() {
        return R.layout.activity_seller_invoice;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        ((RadioGroup) fv(R.id.invoice_rg)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.invoice_rb_company) {
                    type = 2;
                    companyLat.setVisibility(View.VISIBLE);
//                    dm.showAlertDialog("开票须知", " 应国家税务总局要求，自2017年7月1日起，" +
//                            "您若开具增值税普通发票，需同时提供企业抬头及税号，否则发票将无法用于企业报销", "我知道了", new DialogManager.Callback() {
//                        @Override
//                        public void handleLeft() {
//                            startActivity(SellerInvoiceActivity.class, null);
//                        }
//                    });
                } else if (checkedId == R.id.invoice_rb_personal) {
                    type = 1;
                    companyLat.setVisibility(View.GONE);
                }
            }
        });
        fv(R.id.invoice_address_lat).setOnClickListener(this);
        btn = fv(R.id.invoice_btn);
        btn.setOnClickListener(this);

        titleEt = fv(R.id.invoice_title_et);
        numEt = fv(R.id.invoice_num_et);
        remarkEt = fv(R.id.invoice_remark_et);
        contextTv = fv(R.id.invoice_context_tv);
        moneyTv = fv(R.id.invoice_money_tv);
        nameTv = fv(R.id.invoice_name_tv);
        mobileTv = fv(R.id.invoice_mobile_tv);
        addressTv = fv(R.id.invoice_address_tv);
        freightTv = fv(R.id.invoice_freight_tv);
        typeTv = fv(R.id.invoice_type_tv);

        companyLat = fv(R.id.invoice_company_lat);
        addressTopLat = fv(R.id.invoice_address_top_lat);
        getParams();
    }

    private void getParams() {
        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        address = JSON.parseObject(data, AddressEntity.class);
        initPageContext();
    }

    private void initPageContext() {
        JSONObject object = JSON.parseObject(data);
        contextTv.setText(object.getString("desc"));
        moneyTv.setText(object.getString("total_fee"));
        freightTv.setText(object.getString("shipping"));
        typeTv.setText(object.getString("invoice_type"));
        setAddress();
    }

    private void setAddress() {
        if (address != null && address.getConsignee() != null) {
            addressTopLat.setVisibility(View.VISIBLE);
            nameTv.setText(address.getConsignee());
            mobileTv.setText(address.getMobile());
            setAddressTv(address.getAddress_detail());
        } else {
            addressTopLat.setVisibility(View.GONE);
            setAddressTv(getString(R.string.no_address));
        }
    }

    private void setAddressTv(String context) {
        SpannableString spannableString = new SpannableString("邮寄地址：" + context);
        spannableString.setSpan(new ForegroundColorSpan(0xff4c4c4c), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(14, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        addressTv.setText(spannableString);
    }

    private void save() {
        final String title = titleEt.getText().toString();
        final String num = numEt.getText().toString();
        if (title.length() <= 0) {
            showToast("请填写发票抬头");
            return;
        }
        if (type == 2 && num.length() <= 0) {
            showToast("请输入公司税号");
            return;
        }
        dm.showAlertDialog2("请检查填写内容是否准确，检查无误立即申请", "取消", "确定", new DialogManager.Callback() {
            @Override
            public void handleRight() {
                AbstractRequest request = buildRequest(UrlUtils.invoiceAdd, RequestMethod.POST, null);
                request.add("status", getIntent().getIntExtra("status", 0));
                request.add("invoice_ids", getIntent().getStringExtra("invoice_ids"));
                request.add("invoice_type", 1);
                request.add("letterhead_type", type);
                request.add("letterhead", title);
                request.add("invoice_desc", contextTv.getText().toString());
                if (type == 2)
                    request.add("company_tax", num);
                request.add("remark", remarkEt.getText().toString());
                request.add("address_id", address.getAddress_id());
                executeNetwork(0, "请稍后", request);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invoice_address_lat:
                if (address != null && address.getConsignee() != null) {//有地址
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 1);
                    bundle.putInt("address_id", address.getAddress_id());
                    startActivityForResult(AddressActivity.class, bundle, CodeUtils.REQUEST_ADDRESS);
                } else {//没有地址
                    startActivityForResult(AddressAddActivity.class, null, CodeUtils.REQUEST_NO_ADDRESS);
                }
                break;
            case R.id.invoice_btn:
                save();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.REQUEST_ADDRESS || requestCode == CodeUtils.REQUEST_NO_ADDRESS) {
            if (resultCode == Activity.RESULT_OK) {
                address = data.getParcelableExtra("address");
                setAddress();
            }
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            JSONObject jsonObject = JSON.parseObject(result.getData().toString());
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            bundle.putString("info", jsonObject.getString("success_text"));
            startActivity(TextInfoSuccessActivity.class, bundle);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }
}