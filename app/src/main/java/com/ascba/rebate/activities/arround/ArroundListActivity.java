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
    private LocationManager lm;
    private ArroundAdapter arroundAdapter;
    private List<ArroundEntity> arroundEntities;
    private EditText searchEt;
    private TextView btn;

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
        mRefreshLayout.setPrimaryColorsId(R.color.bg, R.color.grey_black_tv);
        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPage++;
                requestNetwork(1);
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
                if (s.length() > 0) {
                    btn.setEnabled(true);
                    btn.setTextColor(getResources().getColor(R.color.blue_btn));
                } else {
                    btn.setEnabled(false);
                    btn.setTextColor(getResources().getColor(R.color.grey_black_tv2));
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
                    Log.d(TAG, "long" + location.getLongitude() + ",lan" + location.getLatitude());
                    lon = location.getLongitude();
                    lat = location.getLatitude();
                    Log.i(TAG, "onLocateSuccess: " + location.getAddress());
                    mPage = 1;
                    requestNetwork(0);
                }
            });
        }
    }

    private void requestNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.sellerArround, RequestMethod.GET, null);
        request.add("lon", lon);
        request.add("lat", lat);
        request.add("paged", mPage);
        executeNetwork(what, "请稍后", request);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.search_tv) {
            // TODO
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            arroundEntities = JSON.parseArray(JSON.parseObject(result.getData().toString())
                    .getString("pushBusinessList"), ArroundEntity.class);
            arroundAdapter.setNewData(arroundEntities);
        } else if (what == 1) {
            arroundEntities = JSON.parseArray(JSON.parseObject(result.getData().toString())
                    .getString("pushBusinessList"), ArroundEntity.class);
            if (arroundEntities.size() == 0) {
                loadMoreOver();
            } else {
                arroundAdapter.addData(arroundEntities);
            }
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
