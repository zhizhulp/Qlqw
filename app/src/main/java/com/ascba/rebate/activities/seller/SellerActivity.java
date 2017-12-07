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
import com.ascba.rebate.activities.trade.ConfirmListActivity;
import com.ascba.rebate.activities.trade.ReceiveCodeActivity;
import com.ascba.rebate.adapter.SellerRecommendedAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.SellerDet;
import com.ascba.rebate.bean.SellerEntity;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.List;

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
                        requestSellerData();
//                        boolean alreadyApply = false;
//                        if (!alreadyApply) {
//                            startActivity(MctApplyStartActivity.class, null);
//                        } else {
//                            startActivity(MctEnterActivity.class, null);
//                        }

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

    private void requestSellerData() {
        AbstractRequest request = buildRequest(UrlUtils.perfect, RequestMethod.GET, SellerDet.class);
        executeNetwork(1, "请稍后", request);
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
                startActivity(ReceiveCodeActivity.class, null);
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

            List<SellerEntity.ServerBean> servers = sellerEntity.getServer();
            servers.add(new SellerEntity.ServerBean("完善商家资料", "完善商家资料，享受商家更多特权",
                    "http://apidebug.qlqwp2p.com/public/static/app/images/StoredOne.png",
                    "http://www.qlqw.com/purchase/giveindex", 4));
            sellerRecommendedAdapter.setNewData(sellerEntity.getServer());
        } else if (what == 1) {
            SellerDet data = (SellerDet) result.getData();
            AppConfig.getInstance().putInt("company_status", data.getCompany_status());
            int sellerStatus = data.getSeller_status();
            if (sellerStatus == 0) {//0商家未完善资料
                MctApplyStartActivity.start(this,data.getPerfect_url());
            } else if (sellerStatus == 1) {//1商家已完善过资料
                MctEnterActivity.start(this,1);
            }
        }
    }
}
