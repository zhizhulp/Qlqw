package com.ascba.rebate.activities.score_buy;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.ScoreBillActivity;
import com.ascba.rebate.activities.score_shop.GiftGoodsDetailsActivity;
import com.ascba.rebate.adapter.ScoreBuyAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.ScoreBuyBanner;
import com.ascba.rebate.bean.ScoreBuyBase;
import com.ascba.rebate.bean.ScoreBuyHead;
import com.ascba.rebate.bean.ScoreBuyMsgP;
import com.ascba.rebate.bean.ScoreBuyType;
import com.ascba.rebate.bean.ScoreHome;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2017/12/29 10:02
 * Describe:礼品采购
 */

public class ScoreBuyHome1Activity extends BaseDefaultNetActivity {
    private int paged = 1;
    private List<ScoreBuyBase> data;
    private FloatingActionButton fb;
    private ScoreBuyAdapter adapter;
    private FrameLayout barLat;
    private int totalY;

    @Override
    protected int bindLayout() {
        return R.layout.activity_score_buy_home1;
    }

    @Override
    protected int setUIMode() {
        return BaseUIActivity.UIMODE_TRANSPARENT_NOTALL;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        fb = fv(R.id.fb);
        setRecyclerView();
        setFloatBar();
        setMoneyBar();
        requestData();
    }

    private void setMoneyBar() {
        barLat = (FrameLayout)findViewById(R.id.lat_bar);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                Bundle bundle = new Bundle();
                bundle.putInt("mine_type", 3);
                startActivity(ScoreBillActivity.class, bundle);
            }
        });
        final int maxY = getResources().getDisplayMetrics().widthPixels;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalY += dy;
                if (totalY <= maxY) {
                    barLat.setBackgroundColor(Color.argb(totalY * 255 / maxY, 64, 143, 255));
                } else {
                    barLat.setBackgroundColor(ContextCompat.getColor(ScoreBuyHome1Activity.this,R.color.blue_btn));
                }

            }
        });
    }

    private void setFloatBar() {
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
    }


    private void setRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        data = new ArrayList<>();
        adapter = new ScoreBuyAdapter(data);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ScoreBuyBase scoreBuyBase = data.get(position);
                if (scoreBuyBase instanceof ScoreHome.GiftGoods) {
                    Bundle b = new Bundle();
                    b.putInt("goods_id", ((ScoreHome.GiftGoods) scoreBuyBase).getGoods_id());
                    startActivity(GiftGoodsDetailsActivity.class, b);
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                if(view.getId()==R.id.tv_more_type){
                    startActivity(GiftGoodsTypeActivity.class,null);
                }
            }
        });
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableScrollContentWhenLoaded(false);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                paged++;
                requestData();
            }
        });
        PinnedHeaderItemDecoration stickyHeader = new PinnedHeaderItemDecoration.Builder(1).create();
        //mRecyclerView.addItemDecoration(stickyHeader);
        mRecyclerView.setAdapter(adapter);
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.purchaseIndex2, RequestMethod.GET, null);
        request.add("paged", paged);
        executeNetwork(0, request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            String data = (String) result.getData();
            JSONObject dataObj = JSON.parseObject(data);
            String bannerStr = dataObj.getString("banner");
            if (paged == 1) {
                //banner
                if (!TextUtils.isEmpty(bannerStr)) {
                    List<ScoreBuyBanner.ScoreBuyImg> banners = JSON.parseArray(bannerStr, ScoreBuyBanner.ScoreBuyImg.class);
                    if (banners != null && banners.size() > 0) {
                        ScoreBuyBanner buyBanner = new ScoreBuyBanner();
                        buyBanner.setImgs(banners);
                        this.data.add(buyBanner);
                    }
                }
                //零售，批发，定制
                String purchaseCate = dataObj.getString("purchase_cate");
                if (!TextUtils.isEmpty(purchaseCate)) {
                    List<ScoreBuyHead.ScoreBuyGrid> grids = JSON.parseArray(purchaseCate, ScoreBuyHead.ScoreBuyGrid.class);
                    if (grids != null && grids.size() > 0) {
                        ScoreBuyHead head = new ScoreBuyHead();
                        head.setGrids(grids);
                        this.data.add(head);
                    }
                }
                //消息
                String log = dataObj.getString("log");
                if (!TextUtils.isEmpty(log)) {
                    List<String> logs = JSON.parseArray(log, String.class);
                    if (logs != null && logs.size() > 0) {
                        ScoreBuyMsgP msgs = new ScoreBuyMsgP();
                        msgs.setMsgs(logs);
                        this.data.add(msgs);
                    }
                }
                //分类
                String goodsCate = dataObj.getString("goods_cate");
                if (!TextUtils.isEmpty(goodsCate)) {
                    List<ScoreBuyType.ScoreBuyTypeC> scoreBuyTypeCS = JSON.parseArray(goodsCate, ScoreBuyType.ScoreBuyTypeC.class);
                    if (scoreBuyTypeCS != null && scoreBuyTypeCS.size() > 0) {
                        ScoreBuyType type = new ScoreBuyType();
                        type.setTypes(scoreBuyTypeCS);
                        this.data.add(type);
                    }
                }
            }
            //商品列表
            String goodsList = dataObj.getString("goods_list");
            List<ScoreHome.GiftGoods> giftGoods = JSON.parseArray(goodsList, ScoreHome.GiftGoods.class);
            if (paged != 1) {
                if (giftGoods == null || giftGoods.size() == 0) {
                    loadMoreOver();
                }
            }
            if (giftGoods != null && giftGoods.size() > 0) {
                this.data.addAll(giftGoods);
            }
            adapter.notifyDataSetChanged();
        }
    }
}