package com.ascba.rebate.activities.arround;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.ArroundAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.ArroundEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.LocationManager;
import com.ascba.rebate.net.AbstractRequest;
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
                // TODO
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
        request.add("paged", mPage);
        if (type == 2)
            request.add("search", searchEt.getText().toString());
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
            List<ArroundEntity> list = JSON.parseArray(JSON.parseObject(result.getData().toString())
                    .getString("pushBusinessList"), ArroundEntity.class);
            arroundAdapter.setNewData(list);
        } else if (what == GET_PAGE) {
            List<ArroundEntity> list = JSON.parseArray(JSON.parseObject(result.getData().toString())
                    .getString("pushBusinessList"), ArroundEntity.class);
            if (list.size() == 0)
                loadMoreOver();
            else
                arroundAdapter.addData(list);
        } else if (what == SEARCH_ONE) {
            List<ArroundEntity> list = JSON.parseArray(JSON.parseObject(result.getData().toString())
                    .getString("pushBusinessList"), ArroundEntity.class);
            searchAdapter.setNewData(list);
        } else if (what == SEARCH_PAGE) {
            List<ArroundEntity> list = JSON.parseArray(JSON.parseObject(result.getData().toString())
                    .getString("pushBusinessList"), ArroundEntity.class);
            if (list.size() == 0)
                loadMoreOver();
            else
                searchAdapter.addData(list);
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
