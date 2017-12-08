package com.ascba.rebate.activities.seller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.BillActivity;
import com.ascba.rebate.activities.merchant.MctApplyStartActivity;
import com.ascba.rebate.activities.merchant.MctEnterActivity;
import com.ascba.rebate.activities.merchant.MctRightsActivity;
import com.ascba.rebate.activities.trade.ConfirmListActivity;
import com.ascba.rebate.activities.trade.ReceiveCodeActivity;
import com.ascba.rebate.adapter.SellerRecommendedAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.SellerEntity;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by Jero on 2017/10/12 0012.
 */

public class SellerActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private SellerRecommendedAdapter sellerRecommendedAdapter;
    private SellerEntity sellerEntity;

    private TextView money;

    @Override
    protected int bindLayout() {
        return R.layout.activity_seller;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mMoneyBar.setTextTail("设置");
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                startActivity(SellerSetActivity.class, null);
            }
        });
        sellerRecommendedAdapter = new SellerRecommendedAdapter();
        mRecyclerView.setAdapter(sellerRecommendedAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sellerRecommendedAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SellerEntity.ServerBean item = (SellerEntity.ServerBean) adapter.getItem(position);
                switch (item.getServer_status()) {
                    case 1:
                        showToast(sellerEntity.getServer_tip());
                        break;
                    case 2:
                        WebViewBaseActivity.start(SellerActivity.this, item.getServer_title(), item.getServer_url());
                        break;
                    case 3:
                        startActivity(SellerGiveCreateActivity.class, null);
                        break;
                    case 4:
                        //todo
                        if (sellerEntity.getMember_status() == 0 || sellerEntity.getMember_status() == 1)
                            startActivity(MctApplyStartActivity.class, null);
                        else if (sellerEntity.getMember_status() == 2 || sellerEntity.getMember_status() == 3)
                            startActivity(MctEnterActivity.class, null);
                        break;
                    case 5:
                        startActivity(MctRightsActivity.class, null);
                        break;
                    default:

                        break;
                }
            }
        });
        sellerRecommendedAdapter.addHeaderView(getTopView());
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setPrimaryColorsId(R.color.blue_btn, android.R.color.white);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {//请求数据
                requestNetwork();
            }
        });
        requestNetwork();
    }

    private void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.purchaseIndex, RequestMethod.GET, SellerEntity.class);
        executeNetwork(0, "请稍后", request);
    }

    private View getTopView() {
        View view = getLayoutInflater().inflate(R.layout.item_seller_top, null);
        view.findViewById(R.id.seller_top_btn1).setOnClickListener(this);
        view.findViewById(R.id.seller_top_btn2).setOnClickListener(this);
        view.findViewById(R.id.seller_top_btn3).setOnClickListener(this);
        view.findViewById(R.id.item_seller_iv).setOnClickListener(this);
        money = (TextView) view.findViewById(R.id.item_money);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seller_top_btn1://我要收款
                //todo
                if (sellerEntity.getMember_status() == 2)
                    startActivity(ReceiveCodeActivity.class, null);
                else
                    dm.showAlertDialog2(sellerEntity.getMember_status_text(), "取消", "确认", new DialogManager.Callback() {
                        @Override
                        public void handleLeft() {
                            super.handleLeft();
                        }

                        @Override
                        public void handleRight() {
                            super.handleRight();
                        }
                    });
                break;
            case R.id.seller_top_btn2://明细记录
                Intent intent = new Intent(this, BillActivity.class);
                intent.putExtra("mine_type", 3);
                startActivity(intent);
                break;
            case R.id.seller_top_btn3://现金确认
                startActivity(ConfirmListActivity.class, null);
                break;
            case R.id.item_seller_iv://立即储值
                startActivity(SellerPurchaseActivity.class, null);
                break;
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            sellerEntity = (SellerEntity) result.getData();
            money.setText(sellerEntity.getMoney());
            sellerRecommendedAdapter.setNewData(sellerEntity.getServer());
        }
    }
}
