package com.ascba.rebate.activities.seller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.BillActivity;
import com.ascba.rebate.activities.merchant.MctApplyStartActivity;
import com.ascba.rebate.activities.merchant.MctEnterActivity;
import com.ascba.rebate.activities.merchant.MctPayActivity;
import com.ascba.rebate.activities.merchant.MctRightsActivity;
import com.ascba.rebate.activities.trade.ConfirmListActivity;
import com.ascba.rebate.activities.trade.ReceiveCodeActivity;
import com.ascba.rebate.adapter.SellerRecommendedAdapter;
import com.ascba.rebate.appconfig.AppConfig;
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
                        int memberStatus = sellerEntity.getMember_status();
                        if (memberStatus == 1) {
                            startActivity(MctApplyStartActivity.class, null);
                        } else if (memberStatus == 2) {
                            MctEnterActivity.start(SellerActivity.this, 1);
                        }
                        break;
                    case 5:
                        startActivity(MctRightsActivity.class, null);
                        break;
                    default:
                        showToast("敬请期待");
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

    private void requestSellerStatus() {
        AbstractRequest request = buildRequest(UrlUtils.getSellerStatus, RequestMethod.GET, null);
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
                requestSellerStatus();
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
            String compaynStatusText = sellerEntity.getCompayn_status_text();
            if(!TextUtils.isEmpty(compaynStatusText)){
                AppConfig.getInstance().putString("company_status_text",compaynStatusText);
                AppConfig.getInstance().putInt("company_status",sellerEntity.getCompany_status());
            }
        } else if (what == 1) {
            String data = (String) result.getData();
            JSONObject dataObj = JSON.parseObject(data);
            int memberStatus = dataObj.getIntValue("member_status");
            final String statusText = dataObj.getString("member_status_text");
            String companyStatusText = dataObj.getString("company_status_text");
            if(!TextUtils.isEmpty(companyStatusText)){
                AppConfig.getInstance().putString("company_status_text",companyStatusText);
                AppConfig.getInstance().putInt("company_status",dataObj.getIntValue("company_status"));
            }
            //0正常1普通用户2资料审核中3商家过期4资料不全，第一次（跳转到H5）5资料不全第N次（跳转到资料提交）
            if (memberStatus == 0) {
                startActivity(ReceiveCodeActivity.class, null);
            } else if (memberStatus == 1) {
                dm.showAlertDialog2(statusText, "取消", "确认", new DialogManager.Callback() {
                    @Override
                    public void handleRight() {
                        startActivity(MctPayActivity.class, null);
                    }
                });
            } else if (memberStatus == 2) {
                showToast(statusText);
            } else if (memberStatus == 3) {
                dm.showAlertDialog2(statusText, "取消", "确认", new DialogManager.Callback() {
                    @Override
                    public void handleRight() {
                        startActivity(MctPayActivity.class, null);
                    }
                });
            } else if (memberStatus == 4) {
                dm.showAlertDialog2(statusText, "取消", "确认", new DialogManager.Callback() {
                    @Override
                    public void handleRight() {
                        startActivity(MctApplyStartActivity.class, null);
                    }
                });
            } else if (memberStatus == 5) {
                dm.showAlertDialog2(statusText, "取消", "确认", new DialogManager.Callback() {
                    @Override
                    public void handleRight() {
                        MctEnterActivity.start(SellerActivity.this, 1);
                    }
                });
            }
        }
    }
}