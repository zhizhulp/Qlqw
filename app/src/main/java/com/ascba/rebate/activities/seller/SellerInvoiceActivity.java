package com.ascba.rebate.activities.seller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.setting.AddressActivity;
import com.ascba.rebate.activities.setting.AddressAddActivity;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.AddressEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.utils.CodeUtils;

/**
 * Created by Jero on 2017/11/25 0025.
 */

public class SellerInvoiceActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private int type = 0;//  0 个人 1 公司

    private EditText titleEt, numEt, remarkEt;
    private TextView contextTv, moneyTv, nameTv, mobileTv, addressTv, freightTv;
    private Button btn;
    private LinearLayout addressTopLat, companyLat;

    @Override
    protected int bindLayout() {
        return R.layout.activity_seller_invoice;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        Log.i(TAG, "initViews: "+getIntent().getStringExtra("data"));
        ((RadioGroup) fv(R.id.invoice_rg)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.invoice_rb_company) {
                    type = 1;
                    companyLat.setVisibility(View.VISIBLE);
//                    dm.showAlertDialog("开票须知", " 应国家税务总局要求，自2017年7月1日起，" +
//                            "您若开具增值税普通发票，需同时提供企业抬头及税号，否则发票将无法用于企业报销", "我知道了", new DialogManager.Callback() {
//                        @Override
//                        public void handleLeft() {
//                            startActivity(SellerInvoiceActivity.class, null);
//                        }
//                    });
                } else if (checkedId == R.id.invoice_rb_personal) {
                    type = 0;
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

        companyLat = fv(R.id.invoice_company_lat);
        addressTopLat = fv(R.id.invoice_address_top_lat);
        initPageContext();
    }

    private void initPageContext() {
        contextTv.setText("居间服务费");
        moneyTv.setText("￥365");
        freightTv.setText("包邮");
        setAddress();
    }

    private void setAddress() {
        if (true) {
            addressTopLat.setVisibility(View.VISIBLE);
            nameTv.setText("俩说的话");
            mobileTv.setText("13333333333");
            setAddressTv("北京市 很长很长的地址很长很长的地址很长很长的地址" +
                    "很长很长的地址很长很长的地址");
        } else {
            addressTopLat.setVisibility(View.GONE);
            setAddressTv("您还未填写收货地址，马上去填写");
        }
    }

    private void setAddressTv(String context) {
        SpannableString spannableString = new SpannableString("邮寄地址：" + context);
        spannableString.setSpan(new ForegroundColorSpan(0xff4c4c4c), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(14, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        addressTv.setText(spannableString);
    }

    private void save() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invoice_address_lat:
                if (false) {//有地址
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 1);
                    bundle.putInt("address_id", 0);
                    startActivityForResult(AddressActivity.class, bundle, CodeUtils.REQUEST_ADDRESS);
                } else {//没有地址
                    startActivityForResult(AddressAddActivity.class, null, CodeUtils.REQUEST_NO_ADDRESS);
                }
                break;
            case R.id.invoice_btn:
                //todo
                save();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.REQUEST_ADDRESS || requestCode == CodeUtils.REQUEST_NO_ADDRESS) {
            if (resultCode == Activity.RESULT_OK) {
                //todo
                AddressEntity address = data.getParcelableExtra("address");
                setAddress();
            }
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        if (what == 1) {

        }
    }
}
