package com.ascba.rebate.activities.trade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.SureOrdersAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.SureOrdersEntity;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2017/10/14 14:59
 * Describe: 确认订单列表
 */

public class ConfirmListActivity extends BaseDefaultNetActivity {
    private SureOrdersAdapter baseAdapter;
    private TextView tobe_sure_tv1, tobe_sure_tv2, tobe_sure_tv3, seller_sure_order_tv4;
    List<SureOrdersEntity.DataListBean> data_list = new ArrayList<>();
    private int paged = 1;//当前页数

    @Override
    protected int bindLayout() {
        return R.layout.activity_confirm_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        initView();
        requestNetwork();
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        baseAdapter = new SureOrdersAdapter(R.layout.item_myallorders, data_list);
        View head = getLayoutInflater().inflate(R.layout.tobesuredorders_head, null);
        tobe_sure_tv1 = (TextView) head.findViewById(R.id.tobe_sure_tv1);
        tobe_sure_tv2 = (TextView) head.findViewById(R.id.tobe_sure_tv22);
        tobe_sure_tv3 = (TextView) head.findViewById(R.id.tobe_sure_tv3);
        seller_sure_order_tv4 = (TextView) head.findViewById(R.id.seller_sure_order_tv4);
        baseAdapter.addHeaderView(head);
        mRecyclerView.setAdapter(baseAdapter);
        baseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ConfirmListActivity.this, TradeConfirmActivity.class);
                int order_id = data_list.get(position).getScan_qrcode_id();
                intent.putExtra("order_id", order_id+"");
                startActivityForResult(intent, CodeUtils.CODE_CONFIRM);
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                paged = 1;
                requestNetwork();
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                paged++;
                requestNetwork();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.CODE_CONFIRM && resultCode == RESULT_OK) {
            paged = 1;
            requestNetwork();
        }
    }

    public void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.list, RequestMethod.GET, null);
        request.add("now_page", paged);
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        SureOrdersEntity sureOrdersEntity = JSON.parseObject(((String) result.getData()), SureOrdersEntity.class);
        if (mRefreshLayout.getState() == RefreshState.Refreshing ||
                mRefreshLayout.getState() == RefreshState.None) {
            if (data_list.size() > 0) {
                data_list.clear();
            }
        }
        SureOrdersEntity.InfoBean info = sureOrdersEntity.getIden_info();
        List<SureOrdersEntity.DataListBean> dataList = sureOrdersEntity.getData_list();
        if (paged != 1) {
            if (dataList == null || dataList.size() == 0) {
                loadMoreOver();
            }
        }
        tobe_sure_tv1.setText(info.getTitle());
        tobe_sure_tv2.setText(info.getTotal_money() + "");
        tobe_sure_tv3.setText("共计" + info.getTotal() + "笔");
        seller_sure_order_tv4.setText(info.getTip());
        this.data_list.addAll(dataList);
        baseAdapter.notifyDataSetChanged();
    }
}
