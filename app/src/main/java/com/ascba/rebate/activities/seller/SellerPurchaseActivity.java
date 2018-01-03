package com.ascba.rebate.activities.seller;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.PurchasePayAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultPayActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.Pay;
import com.ascba.rebate.bean.PurchaseEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by Jero on 2017/10/16 0016.
 */

public class SellerPurchaseActivity extends BaseDefaultPayActivity implements View.OnClickListener {
    private static final int GET = 80;

    private TextView num, info;
    private int type;
    private PurchaseEntity purchaseEntity;
    private PurchasePayAdapter purchasePayAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_seller_purchase;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        setStatusBarColor(getResources().getColor(R.color.purple_bg));
        getParams();
        fv(R.id.seller_purchase_ok).setOnClickListener(this);
        fv(R.id.seller_purchase_url).setOnClickListener(this);
        num = fv(R.id.seller_purchase_num);
        info = fv(R.id.seller_purchase_info);
        purchasePayAdapter = new PurchasePayAdapter();
        mRecyclerView.setAdapter(purchasePayAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        purchasePayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                purchasePayAdapter.setSelect(position);
                num.setText("" + purchasePayAdapter.getSelectItem().getCz_money());
            }
        });
        View view = getLayoutInflater().from(this).inflate(R.layout.item_purchase_foot, null);
        purchasePayAdapter.addFooterView(view);
        View view1 = getLayoutInflater().from(this).inflate(R.layout.item_purchase_foot, null);
        purchasePayAdapter.addFooterView(view1);
        requestNetwork();
    }

    private void getParams() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getInt("type");
            switch (type) {
                case 1:
                    mMoneyBar.setTextTitle("零售");
                    break;
                case 2:
                    mMoneyBar.setTextTitle("批发");
                    break;
                case 3:
                    mMoneyBar.setTextTitle("定制");
                    break;
            }
        } else {
            mMoneyBar.setTextTitle("礼品采购");
        }
    }

    @Override
    protected Class<?> bindPaySuccessPage() {
        return PurchaseSuccessActivity.class;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seller_purchase_ok:
                payUtils.info = purchasePayAdapter.getSelectItem().getCz_rate_money() + "";
                showPayDialog("" + purchasePayAdapter.getSelectItem().getCz_money(), purchaseEntity.getMoney());
                break;
            case R.id.seller_purchase_url:
                WebViewBaseActivity.start(this, "预存须知", purchaseEntity.getAgreement_url());
                break;
        }
    }

    private void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.purchasePay, RequestMethod.GET, PurchaseEntity.class);
        executeNetwork(GET, "请稍后", request);
    }

    @Override
    protected void requestPayInfo(String type, String money, int what) {
        AbstractRequest request = buildRequest(UrlUtils.purchasePayment, RequestMethod.POST, Pay.class);
        request.add("pay_type", type);
        request.add("total_fee", money);
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == GET) {
            purchaseEntity = (PurchaseEntity) result.getData();
            purchasePayAdapter.setNewData(purchaseEntity.getMoney_config());
            num.setText("" + purchasePayAdapter.getSelectItem().getCz_money());
            info.setText(purchaseEntity.getTips());
            AppConfig.getInstance().putInt("is_level_pwd", purchaseEntity.getIs_level_pwd());
        }
    }

    @Override
    protected boolean payIsResult() {
        return true;
    }

    @Override
    protected void onResult(String type, int resultCode) {
        if (resultCode == RESULT_OK) {
            payUtils.clear();
        } else if (resultCode == RESULT_CANCELED) {
            startActivity(SellerGiveCreateActivity.class, null);
            payUtils.clear();
            finish();
        }
    }
}
