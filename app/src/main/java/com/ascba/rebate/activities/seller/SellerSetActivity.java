package com.ascba.rebate.activities.seller;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.picasso.MaskTransformation;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

public class SellerSetActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private ImageView imHead;
    private TextView tvName;
    private TextView tvInfo;

    private String avatar, nickname, agreement_url, status_text;
    private int status;

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

        requestNetwork();
    }

    private void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.sellerSetting, RequestMethod.GET, null);
        executeNetwork(0, "请稍后", request);
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
                WebViewBaseActivity.start(SellerSetActivity.this, "发票帮助", agreement_url);
                break;
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            JSONObject jsonObject = JSON.parseObject(result.getData().toString());
            avatar = jsonObject.getString("avatar");
            nickname = jsonObject.getString("nickname");
            agreement_url = jsonObject.getString("agreement_url");
            status = jsonObject.getInteger("status");
            status_text = jsonObject.getString("status_text");
            Picasso.with(this).load(avatar)
                    .transform(new MaskTransformation(this, R.mipmap.head_loading))
                    .placeholder(R.mipmap.head_loading).into(imHead);
            tvName.setText(nickname);
            tvInfo.setText(status_text);
//            if (status == 0)
//                tvInfo.setVisibility(View.VISIBLE);
//            else if (status == 1)
//                tvInfo.setVisibility(View.GONE);
        }
    }
}
