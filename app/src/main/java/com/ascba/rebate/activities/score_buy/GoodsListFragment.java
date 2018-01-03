package com.ascba.rebate.activities.score_buy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.score_shop.GiftGoodsDetailsActivity;
import com.ascba.rebate.adapter.GiftGoodsAdapter;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.ScoreHome;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 商品分类列表
 */
public class GoodsListFragment extends BaseDefaultNetFragment {

    public GoodsListFragment() {

    }
    private int cateId ;//商品列表类型
    private int paged = 1;
    private List<ScoreHome.GiftGoods> data;
    private GiftGoodsAdapter adapter;

    public static GoodsListFragment getInstance(int cateId) {
        GoodsListFragment fragment = new GoodsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("goods_type", cateId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_goods_list;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initRecyclerView();
        setLoadListener();
        getParams();
        //requestData();
    }

    private void initRecyclerView() {
        mRefreshLayout.autoRefresh();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        data = new ArrayList<>();
        adapter = new GiftGoodsAdapter(R.layout.gift_goods_item, data);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle b = new Bundle();
                b.putInt("goods_id", data.get(position).getGoods_id());
                startActivityForResult(GiftGoodsDetailsActivity.class, b, CodeUtils.REQUEST_SCORE_EXCHANGE);
            }
        });
        mRecyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.gift_goods_empty, mRecyclerView);
    }

    private void setLoadListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                paged = 1;
                requestData();
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                paged++;
                requestData();
            }
        });
    }

    private void getParams() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            cateId = bundle.getInt("goods_type");
        }
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.scoregoodsByCate, RequestMethod.GET, null);
        request.add("paged", paged);
        request.add("cate_id", cateId);
        executeNetwork(0, request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            if (mRefreshLayout.getState() == RefreshState.Refreshing ||
                    mRefreshLayout.getState() == RefreshState.None) {
                if (data.size() > 0) {
                    data.clear();
                }
            }
            String dataStr = (String) result.getData();
            JSONObject dataObj = JSON.parseObject(dataStr);
            String goodsListStr = dataObj.getString("goods_list");
            List<ScoreHome.GiftGoods> giftGoodses= JSON.parseArray(goodsListStr,ScoreHome.GiftGoods.class);
            if (paged != 1) {
                if (giftGoodses == null || giftGoodses.size() == 0) {
                    loadMoreOver();
                }
            }
            if (giftGoodses != null && giftGoodses.size() > 0) {
                data.addAll(giftGoodses);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.REQUEST_SCORE_EXCHANGE && resultCode == RESULT_OK) {
            paged = 1;
            requestData();
        }
    }
}
