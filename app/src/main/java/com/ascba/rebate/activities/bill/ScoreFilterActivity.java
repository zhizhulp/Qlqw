package com.ascba.rebate.activities.bill;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.FilterBillAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.Bill;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.datepicker.TimePickerView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ScoreFilterActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private FilterBillAdapter adapter;
    private List<Bill> data;
    private int paged = 1;//当前页数
    private TextView tvTime;
    private TextView tvIn;
    private TextView tvOut;
    private String date;
    private String type;
    private TimePickerView pvTime;

    @Override
    protected int bindLayout() {
        return R.layout.activity_filter_score;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        getParams();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        data = new ArrayList<>();
        adapter = new FilterBillAdapter(R.layout.bill_item, data);
        View headView = getLayoutInflater().inflate(R.layout.filter_bill_head, mRecyclerView, false);
        headView.findViewById(R.id.im_calendar).setOnClickListener(this);
        tvTime = ((TextView) headView.findViewById(R.id.tv_time));
        String[] split = date.split("-");
        tvTime.setText(split[0] + "年" + split[1] + "月");
        tvIn = ((TextView) headView.findViewById(R.id.tv_in));
        tvOut = ((TextView) headView.findViewById(R.id.tv_out));
        adapter.setHeaderAndEmpty(true);
        adapter.addHeaderView(headView);
        adapter.setEmptyView(R.layout.empty_bill, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
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
        requestData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_calendar:
                showDataPickerDialog();
                break;
        }
    }

    private void getParams() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        date = bundle.getString("date");
        type = bundle.getString("type");
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.billscore, RequestMethod.GET, null);
        request.add("paged", paged);
        request.add("type", type);
        request.add("date", date);
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
            String str = (String) result.getData();
            JSONObject strObj = JSON.parseObject(str);
            String billStr = strObj.getString("bill_list");
            List<Bill> billList = JSON.parseArray(billStr, Bill.class);
            if (paged != 1) {
                if (billList == null || billList.size() == 0) {
                    loadMoreOver();
                }
            }
            if (billList != null && billList.size() > 0) {
                data.addAll(billList);
            }
            JSONObject infoObj = strObj.getJSONObject("info");
            if (infoObj != null) {
                tvIn.setText(infoObj.getString("income"));
                tvOut.setText(infoObj.getString("expend"));
            }
            adapter.notifyDataSetChanged();
        }
    }


    /**
     * 时间筛选dialog
     */
    private void showDataPickerDialog() {
        String[] split = date.split("-");
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(Integer.parseInt(split[0]),
                Integer.parseInt(split[1]) - 1,
                selectedDate.get(Calendar.DAY_OF_MONTH));
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
        //时间选择器
        if (pvTime == null) {
            pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    @SuppressLint("SimpleDateFormat")
                    String showFormat = new SimpleDateFormat("yyyy年MM月").format(date);
                    @SuppressLint("SimpleDateFormat")
                    String dateFormat = new SimpleDateFormat("yyyy-MM").format(date);
                    paged = 1;
                    data.clear();
                    ScoreFilterActivity.this.date = dateFormat;
                    tvTime.setText(showFormat);
                    requestData();
                }
            })
                    //年月日时分秒 的显示与否，不设置则默认全部显示
                    .setType(new boolean[]{true, true, false, false, false, false})
                    .setLabel("", "", "", "", "", "")
                    .isCenterLabel(false)
                    .setDividerColor(ContextCompat.getColor(this, R.color.grey_line))
                    .setContentSize(21)
                    .setDate(selectedDate)
                    .setRangDate(startDate, endDate)
                    .setDecorView(null)
                    .setLineSpacingMultiplier(2.0f)
                    .build();
        }
        pvTime.show();
    }
}
