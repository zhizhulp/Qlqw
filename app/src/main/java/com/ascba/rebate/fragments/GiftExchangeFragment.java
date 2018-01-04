package com.ascba.rebate.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.score_shop.GiftGoodsDetailsActivity;
import com.ascba.rebate.adapter.GiftGoodsAdapter;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.ScoreHome;
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

import static android.app.Activity.RESULT_OK;

/**
 * 礼品商城列表
 */
public class GiftExchangeFragment extends BaseDefaultNetFragment {
    private int goodsType = 1;//1表示积分商品2表示礼品券商品
    private int paged = 1;
    private List<ScoreHome.GiftGoods> data;
    private GiftGoodsAdapter adapter;
    private Listener listener;

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static GiftExchangeFragment getInstance(int goodsType) {
        Log.d("GiftExchangeFragment", "getInstance: " + goodsType);
        GiftExchangeFragment fragment = new GiftExchangeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("goods_type", goodsType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_gift_exchange;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initRecyclerView();
        setLoadListener();
        getParams();
        requestData();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        data = new ArrayList<>();
        adapter = new GiftGoodsAdapter(R.layout.gift_goods_item, data);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle b = new Bundle();
                b.putInt("goods_id", data.get(position).getGoods_id());
                b.putInt("scene", 1);
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
            goodsType = bundle.getInt("goods_type");
        }
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.scoreGoodsList, RequestMethod.GET, ScoreHome.class);
        request.add("paged", paged);
        request.add("type", goodsType);
        Log.d(TAG, "requestData: " + goodsType + "," + this);
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
            ScoreHome scoreHome = (ScoreHome) result.getData();
            if (listener != null && scoreHome != null) {
                listener.backData(scoreHome);
            }
            List<ScoreHome.GiftGoods> giftGoodses = scoreHome.getGoods_list();
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

    public interface Listener {
        void backData(ScoreHome scoreHome);
    }
}
