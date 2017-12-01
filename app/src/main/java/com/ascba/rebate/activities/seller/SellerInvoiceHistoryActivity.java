package com.ascba.rebate.activities.seller;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.InvoiceHistoryAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.InvoiceHistoryEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.EmptyUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.List;

/**
 * Created by Jero on 2017/11/27 0027.
 */

public class SellerInvoiceHistoryActivity extends BaseDefaultNetActivity {
    private final int LOAD = 302;
    private final int GET = 376;

    private InvoiceHistoryAdapter invoiceHistoryAdapter;
    private List<InvoiceHistoryEntity> invoiceHistoryEntities;
    private int mPage = 1;

    @Override
    protected int bindLayout() {
        return R.layout.activity_seller_invoice_history;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPage++;
                requestNetwork(LOAD);
            }
        });
        invoiceHistoryAdapter = new InvoiceHistoryAdapter();
        invoiceHistoryAdapter.setEmptyView(new EmptyUtils().getView(this, R.mipmap.empty_invoice, "您还没有开票记录哦~"));
        invoiceHistoryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.item_exchange_logistics)
                    WebViewBaseActivity.start(SellerInvoiceHistoryActivity.this, "物流信息",
                            ((InvoiceHistoryEntity) adapter.getItem(position)).getShipping_url());
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(invoiceHistoryAdapter);
        requestNetwork(GET);
    }

    private void requestNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.invoiceList, RequestMethod.GET, null);
        request.add("paged", mPage);
        executeNetwork(what, request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == GET) {
            invoiceHistoryEntities = JSON.parseArray(JSON.parseObject((String) result.getData()).getString("list"), InvoiceHistoryEntity.class);
            invoiceHistoryAdapter.setNewData(invoiceHistoryEntities);
        } else if (what == LOAD) {
            invoiceHistoryEntities = JSON.parseArray(JSON.parseObject((String) result.getData()).getString("list"), InvoiceHistoryEntity.class);
            if (invoiceHistoryEntities.size() == 0) {
                loadMoreOver();
            } else {
                invoiceHistoryAdapter.addData(invoiceHistoryEntities);
            }
        }
    }
}
