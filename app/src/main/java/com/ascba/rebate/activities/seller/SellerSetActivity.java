package com.ascba.rebate.activities.seller;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.view.picasso.MaskTransformation;
import com.squareup.picasso.Picasso;

public class SellerSetActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private ImageView imHead;
    private TextView tvName;
    private TextView tvInfo;

    @Override
    protected int bindLayout() {
        return R.layout.activity_seller_set;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        fv(R.id.lat_sound).setOnClickListener(this);
        fv(R.id.lat_invoice).setOnClickListener(this);
        fv(R.id.lat_invoice_history).setOnClickListener(this);
        fv(R.id.lat_invoice_help).setOnClickListener(this);
        fv(R.id.lat_seller_head).setOnClickListener(this);

        imHead = fv(R.id.seller_set_head);
        tvName = fv(R.id.seller_set_name);
        tvInfo = fv(R.id.seller_set_info);

        Picasso.with(this).load(AppConfig.getInstance().getString("avatar", null))
                .transform(new MaskTransformation(this, R.mipmap.head_loading))
                .placeholder(R.mipmap.head_loading).into(imHead);
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lat_seller_head:
                break;
            case R.id.lat_sound:
                break;
            case R.id.lat_invoice:
                startActivity(SellerInvoiceListActivity.class, null);
                break;
            case R.id.lat_invoice_history:
                startActivity(SellerInvoiceHistoryActivity.class, null);
                break;
            case R.id.lat_invoice_help:
                //todo
                WebViewBaseActivity.start(SellerSetActivity.this, "发票帮助", "");
                break;
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {

        }
    }
}
