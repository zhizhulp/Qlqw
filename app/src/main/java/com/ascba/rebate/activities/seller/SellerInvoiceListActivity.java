package com.ascba.rebate.activities.seller;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.InvoiceSelectAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.InvoiceSelect;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.EmptyUtils;
import com.ascba.rebate.utils.NumberFormatUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jero on 2017/11/27 0027.
 */

public class SellerInvoiceListActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private static final int GET = 704;
    private static final int LOAD = 698;
    private static final int SAVE = 918;

    private List<InvoiceSelect> invoiceSelects;
    private int num;
    private float total_fee;
    private String agreement_url;

    private InvoiceSelectAdapter invoiceSelectAdapter;
    private RelativeLayout latBottom;
    private CheckBox cbAllSelect;
    private TextView tvNum, tvTotalFee;

    private int selectNum = 0;
    private float selectMoney = 0f;

    @Override
    protected int bindLayout() {
        return R.layout.activity_seller_invoice_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                WebViewBaseActivity.start(SellerInvoiceListActivity.this, "开票说明", agreement_url);
            }
        });
        fv(R.id.invoice_btn).setOnClickListener(this);
        latBottom = fv(R.id.invoice_list_bottom_lat);
        tvNum = fv(R.id.invoice_select_tv);
        tvTotalFee = fv(R.id.invoice_money_tv);
        cbAllSelect = fv(R.id.invoice_all_select);
        cbAllSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectNum = num;
                    selectMoney = total_fee;
                } else {
                    selectNum = 0;
                    selectMoney = 0f;
                }
                setSelectText();
                allItemCheck(isChecked);
            }
        });

        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                invoiceSelectAdapter.paged++;
                requestNetwork(LOAD);
            }
        });

        invoiceSelects = new ArrayList<>();
        invoiceSelectAdapter = new InvoiceSelectAdapter(invoiceSelects);
        invoiceSelectAdapter.setEmptyView(new EmptyUtils().getView(this, R.mipmap.empty_invoice, "您还没有可申请发票哦~"));
        invoiceSelectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                InvoiceSelect select = (InvoiceSelect) adapter.getItem(position);
                if (select.getItemType() == InvoiceSelectAdapter.TYPE_ITEM) {
                    checkSelect(select);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(invoiceSelectAdapter);

        requestNetwork(GET);
    }

    private void allItemCheck(boolean check) {
        for (InvoiceSelect select : invoiceSelects) {
            select.setSelect(check);
        }
        invoiceSelectAdapter.notifyDataSetChanged();
    }

    private void setSelectText() {
        tvNum.setText(""+selectNum);
        tvTotalFee.setText(NumberFormatUtils.getNewFloat(selectMoney));
    }

    private void checkSelect(InvoiceSelect select) {
        select.setSelect(!select.isSelect());
        if (select.isSelect()) {
            selectNum++;
            selectMoney += select.getMoney();
        } else {
            selectNum--;
            selectMoney -= select.getMoney();
        }
        setSelectText();
    }

    private void requestNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.invoiceCreate, RequestMethod.GET, null);
        request.add("paged", invoiceSelectAdapter.paged);
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == GET) {
            JSONObject jsonObject = JSON.parseObject(result.getData().toString());
            List<InvoiceSelect> list = JSON.parseArray(jsonObject.getString("bill_list"), InvoiceSelect.class);
            if (list.size() == 0) {
                latBottom.setVisibility(View.GONE);
                invoiceSelects.clear();
            } else {
                latBottom.setVisibility(View.VISIBLE);
                formatList(list);
                num = jsonObject.getInteger("num");
                total_fee = jsonObject.getFloat("total_fee");
                agreement_url = jsonObject.getString("agreement_url");
            }
            invoiceSelectAdapter.notifyDataSetChanged();
        } else if (what == LOAD) {
            List<InvoiceSelect> list = JSON.parseArray(JSON.parseObject(result.getData().toString())
                    .getString("bill_list"), InvoiceSelect.class);
            if (list.size() == 0)
                loadMoreOver();
            else
                formatList(list);
            invoiceSelectAdapter.notifyDataSetChanged();
        }
    }

    private void formatList(List<InvoiceSelect> list) {
        for (InvoiceSelect item : list) {
            if (!item.getMonth().equals(invoiceSelectAdapter.lastMonth)
                    || !item.getYear().equals(invoiceSelectAdapter.lastYear)) {
                invoiceSelectAdapter.lastMonth = item.getMonth();
                invoiceSelectAdapter.lastYear = item.getYear();
                invoiceSelects.add(new InvoiceSelect(item.getMonth(), item.getYear()));
            }
            invoiceSelects.add(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.invoice_btn) {
//            invoiceSelectAdapter.paged = 1;
//            requestNetwork(LOAD);
//            dm.showAlertDialog2("您的发票金额低于500元，将以顺风到" +
//                    "付的形式快递给你，确认提交吗？", "取消", "确定", new DialogManager.Callback() {
//                @Override
//                public void handleLeft() {
//
//                }
//
//                @Override
//                public void handleRight() {
//                    startActivity(SellerInvoiceActivity.class, null);
//                }
//            });
        }
    }
}
