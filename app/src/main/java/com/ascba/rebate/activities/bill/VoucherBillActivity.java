package com.ascba.rebate.activities.bill;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.benefits.BenefitDetActivity;
import com.ascba.rebate.adapter.BillAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.Bill;
import com.ascba.rebate.bean.BillFilter;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.BillFilterDialog;
import com.ascba.rebate.view.datepicker.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.oushangfeng.pinnedsectionitemdecoration.callback.OnHeaderClickListener;
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

/**
 * Created by 李平 on 2017/10/27 15:32
 * Describe: 福利账单
 */

public class VoucherBillActivity extends BaseDefaultNetActivity {

    private BillAdapter adapter;
    private List<Bill> data;
    private List<BillFilter> filterData;
    private int paged = 1;//当前页数
    private String type = "0";//类型
    private String lastYear;
    private String lastMonth;
    private BillFilterDialog dialog;
    private TimePickerView pvTime;
    private int mineType;

    @Override
    protected int bindLayout() {
        return R.layout.activity_bill_voucher;
    }

    OnHeaderClickListener headerClickListener = new OnHeaderClickListener() {
        @Override
        public void onHeaderClick(View view, int id, int position) {
            if (id == R.id.im_calendar) {
                showDataPickerDialog();
            }
        }

        @Override
        public void onHeaderLongClick(View view, int id, int position) {
        }

        @Override
        public void onHeaderDoubleClick(View view, int id, int position) {
        }
    };

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        getParams();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        data = new ArrayList<>();
        adapter = new BillAdapter(data);
        adapter.setEmptyView(R.layout.empty_bill, mRecyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bill bill = data.get(position);
                String type = bill.getType();
                if (TextUtils.equals("2", type)) {//分红详情
                    BenefitDetActivity.jumpIntent(VoucherBillActivity.this, bill.getObject_id());
                }
            }
        });
        PinnedHeaderItemDecoration stickyHeader = new PinnedHeaderItemDecoration.Builder(BillAdapter.TYPE_HEAD).setClickIds(R.id.im_calendar)
                .setHeaderClickListener(headerClickListener).create();
        stickyHeader.setmOnHeaderChangeListener(new PinnedHeaderItemDecoration.OnHeaderChangeListener() {
            @Override
            public void onChange(int position, View headView) {
                headView.findViewById(R.id.im_calendar).setVisibility(View.VISIBLE);
                mRecyclerView.invalidateItemDecorations();
            }
        });
        mRecyclerView.addItemDecoration(stickyHeader);
        mRecyclerView.setAdapter(adapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                resetParams();
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
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                if (dialog == null) {
                    dialog = new BillFilterDialog(VoucherBillActivity.this, filterData);
                    dialog.setCallback(new BillFilterDialog.Callback() {
                        @Override
                        public void onItemClick(BillFilter bf) {
                            dialog.dismiss();
                            resetParams();
                            type = bf.getType_value();
                            requestData();
                        }
                    });
                }
                dialog.show();
            }
        });
        requestData();
    }

    private void getParams() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mineType = extras.getInt("mine_type");
            switch (mineType) {
                case 1:
                    type = "1";
                    break;
                case 2:
                    type = "2";
                    break;
                case 8:
                    type = extras.getString("type");
                    mMoneyBar.setTextTitle(extras.getString("title", "福利券账单"));
                    mMoneyBar.setTailShow(false);
                    break;
            }
        }
    }

    private void resetParams() {
        lastYear = null;
        lastMonth = null;
        paged = 1;
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.billvoucher, RequestMethod.GET, null);
        request.add("paged", paged);
        request.add("type", type);
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
            List<Bill> sortBillList = sortBillList(billList);
            if (sortBillList != null && sortBillList.size() > 0) {
                data.addAll(sortBillList);
            }
            String choiceType = strObj.getString("choice_type");
            List<BillFilter> bfs = JSON.parseArray(choiceType, BillFilter.class);
            if (bfs != null && bfs.size() > 0) {
                bfs.get(0).setSelect(true);
                if (filterData == null) {
                    filterData = new ArrayList<>();
                    filterData.addAll(bfs);
                }
            }
            if (filterData != null && filterData.size() > 0) {
                mMoneyBar.setTextTail("筛选");
                mMoneyBar.setTailShow(true);
            } else {
                mMoneyBar.setTailShow(false);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private List<Bill> sortBillList(List<Bill> billList) {
        if (billList == null || billList.size() == 0) {
            return null;
        }
        List<Bill> sortList = new ArrayList<>();
        for (int i = 0; i < billList.size(); i++) {
            Bill bill = billList.get(i);
            String month = bill.getMonth();
            String year = bill.getYear();
            if (!month.equals(lastMonth) || !year.equals(lastYear)) {
                addHead(sortList, bill);
            }
            lastMonth = bill.getMonth();
            lastYear = bill.getYear();
            addItem(sortList, bill);
        }
        return sortList;
    }

    private void addItem(List<Bill> sortList, Bill bill) {
        bill.setItemType(BillAdapter.TYPE_ITEM);
        sortList.add(bill);
    }

    private void addHead(List<Bill> sortList, Bill bill) {
        Bill bill1 = new Bill();
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int severMonth = Integer.parseInt(bill.getMonth());
        int severYear = Integer.parseInt(bill.getYear());
        if (year == severYear) {
            if (severMonth == month) {
                bill1.setMonth("本月");
            } else {
                bill1.setMonth(severMonth + "月");
            }
        } else {
            bill1.setMonth(severYear + "年" + severMonth + "月");
        }
        bill1.setItemType(BillAdapter.TYPE_HEAD);
        sortList.add(bill1);
    }

    /**
     * 时间筛选dialog
     */
    private void showDataPickerDialog() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
        //时间选择器
        if (pvTime == null) {
            pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    Bundle bundle = new Bundle();
                    @SuppressLint("SimpleDateFormat")
                    String format = new SimpleDateFormat("yyyy-MM").format(date);
                    bundle.putString("date", format);
                    bundle.putString("type", type);
                    startActivity(VoucherFilterActivity.class, bundle);
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
