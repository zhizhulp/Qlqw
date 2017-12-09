package com.ascba.rebate.activities.arround;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.merchant.MctApplyStartActivity;
import com.ascba.rebate.activities.merchant.MctPayActivity;
import com.ascba.rebate.adapter.ArroundAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.ArroundEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.LocationManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.EmptyUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.List;

/**
 * Created by Jero on 2017/12/5 0005.
 */

public class ArroundListActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private static final int GET_ONE = 195;
    private static final int GET_PAGE = 177;
    private static final int SEARCH_ONE = 376;
    private static final int SEARCH_PAGE = 563;

    private LocationManager lm;
    private ArroundAdapter arroundAdapter;
    private ArroundAdapter searchAdapter;
    private EditText searchEt;
    private TextView btn;

    private int type = 1;// 1 周边 2查询
    private int mSearchPage = 1;
    private int mPage = 1;
    private double lon, lat;

    private int member_status;//0普通用户1未完善资料2已完善资料3商家过期

    @Override
    protected int bindLayout() {
        return R.layout.activity_arround_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {

            @Override
            public void clickTail() {
                switch (member_status) {
                    case 1:
                        startActivity(MctPayActivity.class, null);
                        break;
                    case 2:
                        startActivity(MctApplyStartActivity.class, null);
                        break;
                    case 3:
                        startActivity(MctPayActivity.class, null);
                        break;
                }
            }
        });
        mRefreshLayout.setPrimaryColorsId(R.color.bg, R.color.grey_black_tv2);
        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                // 根据状态请求不同的加载更多
                if (type == 1) {
                    mPage++;
                    requestNetwork(GET_PAGE);
                } else if (type == 2) {
                    mSearchPage++;
                    requestNetwork(SEARCH_PAGE);
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (null != lm) {
                    lm.destroyLocation();
                }
                lm = null;
                startLocation();
            }
        });
        searchAdapter = new ArroundAdapter();
        searchAdapter.setEmptyView(new EmptyUtils().getView(this, R.mipmap.no_award, "没有搜索到商家哦~"));
        arroundAdapter = new ArroundAdapter();
        mRecyclerView.setAdapter(arroundAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        btn = fv(R.id.search_tv);
        btn.setOnClickListener(this);
        searchEt = fv(R.id.search_et);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //根据搜索文字长度 决定是否开启搜索按钮
                if (s.length() > 0) {
                    if (!btn.isEnabled()) {
                        btn.setEnabled(true);
                        btn.setTextColor(getResources().getColor(R.color.blue_btn));
                    }
                } else {
                    btn.setEnabled(false);
                    btn.setTextColor(getResources().getColor(R.color.grey_black_tv2));
                    //如果搜索长度清空并且显示内容为搜索内容  切换为原来的内容
                    if (type == 2) {
                        type = 1;
                        mRecyclerView.setAdapter(arroundAdapter);
                    }
                }
            }
        });

        startLocation();
    }

    private void startLocation() {
        if (lm == null) {
            lm = new LocationManager(this, new LocationManager.LocateListener() {
                @Override
                public void onLocateSuccess(AMapLocation location) {
                    lon = location.getLongitude();
                    lat = location.getLatitude();
                    // 根据状态决定 是那种类型的初始化加载
                    if (type == 1) {
                        mPage = 1;
                        requestNetwork(GET_ONE);
                    } else if (type == 2) {
                        mSearchPage = 1;
                        requestNetwork(SEARCH_ONE);
                    }
                }

                @Override
                public boolean onLocateFailed(AMapLocation location) {
                    if (type == 1) {
                        mPage = 1;
                        requestNetwork(GET_ONE);
                    } else if (type == 2) {
                        mSearchPage = 1;
                        requestNetwork(SEARCH_ONE);
                    }
                    return false;
                }
            });
        }
    }

    private void requestNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.sellerArround, RequestMethod.GET, null);
        request.add("lon", lon);
        request.add("lat", lat);
        if (type == 2) {
            request.add("paged", mSearchPage);
            request.add("search", searchEt.getText().toString());
        } else
            request.add("paged", mPage);
        executeNetwork(what, "请稍后", request);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.search_tv) {
            type = 2;
            requestNetwork(SEARCH_ONE);
            // 清空上次搜索数据
            mSearchPage = 1;
            searchAdapter.getData().clear();
            mRecyclerView.setAdapter(searchAdapter);
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == GET_ONE) {
            JSONObject object = JSON.parseObject(result.getData().toString());
            List<ArroundEntity> list = JSON.parseArray(object.getString("pushBusinessList"), ArroundEntity.class);
            member_status = object.getInteger("member_status");
            setTail();
            arroundAdapter.setNewData(list);
        } else if (what == GET_PAGE) {
            JSONObject object = JSON.parseObject(result.getData().toString());
            List<ArroundEntity> list = JSON.parseArray(object.getString("pushBusinessList"), ArroundEntity.class);
            member_status = object.getInteger("member_status");
            setTail();
            if (list.size() == 0)
                loadMoreOver();
            else
                arroundAdapter.addData(list);
        } else if (what == SEARCH_ONE) {
            JSONObject object = JSON.parseObject(result.getData().toString());
            List<ArroundEntity> list = JSON.parseArray(object.getString("pushBusinessList"), ArroundEntity.class);
            member_status = object.getInteger("member_status");
            setTail();
            searchAdapter.setNewData(list);
        } else if (what == SEARCH_PAGE) {
            JSONObject object = JSON.parseObject(result.getData().toString());
            List<ArroundEntity> list = JSON.parseArray(object.getString("pushBusinessList"), ArroundEntity.class);
            member_status = object.getInteger("member_status");
            setTail();
            if (list.size() == 0)
                loadMoreOver();
            else
                searchAdapter.addData(list);
        }
    }

    private void setTail() {
        switch (member_status) {
            case 0:
                mMoneyBar.setTextTail("");
                break;
            case 1:
                mMoneyBar.setTextTail("商家入驻");
                break;
            case 2:
                mMoneyBar.setTextTail("完善资料");
                break;
            case 3:
                mMoneyBar.setTextTail("商家续费");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != lm) {
            lm.destroyLocation();
        }
    }
}
