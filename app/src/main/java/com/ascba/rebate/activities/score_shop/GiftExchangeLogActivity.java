package com.ascba.rebate.activities.score_shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.GiftExchangeLogAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.ExchangeEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.List;

/**
 * Created by Jero on 2017/9/22 0022.
 */

public class GiftExchangeLogActivity extends BaseDefaultNetActivity {

    private GiftExchangeLogAdapter exchangeAdapter;
    private int mPage = 1;
    private List<ExchangeEntity> exchangeList;
    private int type = 1;//1积分2福利券

    @Override
    protected int bindLayout() {
        return R.layout.activity_gift_exchange;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        exchangeAdapter = new GiftExchangeLogAdapter();
        mRecyclerView.setAdapter(exchangeAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_exchange_logistics:
                        WebViewBaseActivity.start(GiftExchangeLogActivity.this, "物流信息",
                                ((ExchangeEntity) adapter.getItem(position)).getStatus_url());
                        break;
                }
            }
        });
        exchangeAdapter.bindToRecyclerView(mRecyclerView);
        exchangeAdapter.setEmptyView(R.layout.no_exchange);
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setPrimaryColorsId(R.color.blue_btn, android.R.color.white);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {//请求数据
                mPage = 1;
                requestData(0);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPage++;
                requestData(1);
            }
        });
        getParams();
        requestData(0);
    }

    private void getParams() {
        type = getIntent().getIntExtra("type", 1);
    }

    public static void jumpIntent(Context context, int type) {
        Intent intent=new Intent(context,GiftExchangeLogActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    private void requestData(int what) {
        AbstractRequest request = buildRequest(UrlUtils.exchangeLog, RequestMethod.GET, null);
        request.add("paged", mPage);
        request.add("type", type);
        executeNetwork(what, request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        switch (what) {
            case 0:
                exchangeList = JSON.parseArray(JSON.parseObject((String) result.getData()).getString("log_list"), ExchangeEntity.class);
                exchangeAdapter.setNewData(exchangeList);
                break;
            case 1:
                exchangeList = JSON.parseArray(JSON.parseObject((String) result.getData()).getString("log_list"), ExchangeEntity.class);
                if (exchangeList.size() == 0) {
                    loadMoreOver();
                } else {
                    exchangeAdapter.addData(exchangeList);
                }
                break;
        }
    }
}
