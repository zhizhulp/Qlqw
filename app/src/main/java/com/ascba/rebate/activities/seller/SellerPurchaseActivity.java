package com.ascba.rebate.activities.seller;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.ScoreBillActivity;
import com.ascba.rebate.adapter.PurchaseMoneyAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultPayActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.Pay;
import com.ascba.rebate.bean.PurchaseEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by Jero on 2017/10/16 0016.
 */

public class SellerPurchaseActivity extends BaseDefaultPayActivity implements View.OnClickListener {
    private static final int GET = 80;

    private TextView num, info;
    private GridView gridView;

    private PurchaseMoneyAdapter purchaseMoneyAdapter;
    private PurchaseEntity purchaseEntity;

    @Override
    protected int bindLayout() {
        return R.layout.activity_seller_purchase;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mMoneyBar.setTailShow(true);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                Bundle bundle = new Bundle();
                bundle.putInt("mine_type", 3);
                startActivity(ScoreBillActivity.class, bundle);
            }
        });
        fv(R.id.seller_purchase_ok).setOnClickListener(this);
        fv(R.id.seller_purchase_url).setOnClickListener(this);
        num = fv(R.id.seller_purchase_num);
        info = fv(R.id.seller_purchase_info);
        purchaseMoneyAdapter = new PurchaseMoneyAdapter(this, null);
        gridView = fv(R.id.seller_purchase_grid);
        gridView.setAdapter(purchaseMoneyAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                purchaseMoneyAdapter.setSelect(position);
                purchaseMoneyAdapter.notifyDataSetChanged();
                num.setText("" + purchaseMoneyAdapter.getSelectItem().getCz_rate_money());
            }
        });
        requestNetwork();
    }

    @Override
    protected Class<?> bindPaySuccessPage() {
        return PurchaseSuccessActivity.class;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seller_purchase_ok:
                payUtils.info = purchaseMoneyAdapter.getSelectItem().getCz_rate_money() + "";
                showPayDialog("" + purchaseMoneyAdapter.getSelectItem().getCz_money(), purchaseEntity.getMoney());
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
            purchaseMoneyAdapter.setData(purchaseEntity.getMoney_config());
            num.setText("" + purchaseMoneyAdapter.getSelectItem().getCz_rate_money());
            info.setText(purchaseEntity.getTips());
            AppConfig.getInstance().putInt("is_level_pwd", purchaseEntity.getIs_level_pwd());
            setGridHeight(purchaseEntity.getMoney_config().size());
        }
    }

    private void setGridHeight(int size) {
        if (size == 0)
            return;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) gridView.getLayoutParams();
        int itemHeight = (int) ScreenDpiUtils.dp2px(this, ++size / 2 * 53 - 10);
        int spaceHeight = getResources().getDisplayMetrics().heightPixels
                - (int) ScreenDpiUtils.dp2px(this, 25 + 56 + 54 + 59 + 84 + 13 + 83 + 10);
        params.height = itemHeight > spaceHeight ? spaceHeight : itemHeight;
        gridView.setLayoutParams(params);
    }
}
