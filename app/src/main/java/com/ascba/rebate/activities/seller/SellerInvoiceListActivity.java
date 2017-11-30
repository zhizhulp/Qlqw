package com.ascba.rebate.activities.seller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CheckBox;
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
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.EmptyUtils;
import com.ascba.rebate.utils.NumberFormatUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.yanzhenjie.nohttp.RequestMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jero on 2017/11/27 0027.
 */

public class SellerInvoiceListActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private final int GET = 704;
    private final int LOAD = 698;
    private final int SAVE = 918;
    private final int RESULT = 249;

    private List<InvoiceSelect> invoiceSelects;
    private int num;
    private float total_fee;
    private String agreement_url;

    private InvoiceSelectAdapter invoiceSelectAdapter;
    private RelativeLayout latBottom;
    private CheckBox cbAllSelect;
    private TextView tvNum, tvMoney;

    private int selectNum;
    private float selectMoney;
    private BigDecimal moneyBigDecimal;

    private int status;
    private String invoice_ids;

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
        tvMoney = fv(R.id.invoice_money_tv);
        cbAllSelect = fv(R.id.invoice_all_select);
        cbAllSelect.setOnClickListener(this);

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
                    cbAllSelect.setChecked(false);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(invoiceSelectAdapter);

        initSelect();
        requestNetwork(GET);
    }

    private void allItemCheck(boolean check) {
        for (InvoiceSelect select : invoiceSelects) {
            if (select.getItemType() == InvoiceSelectAdapter.TYPE_ITEM)
                select.setSelect(check);
        }
        invoiceSelectAdapter.notifyDataSetChanged();
    }

    private void initSelect() {
        selectNum = 0;
        setSelectMoney(0);
    }

    private void setSelectText() {
        tvNum.setText("" + selectNum);
        tvMoney.setText(NumberFormatUtils.getNewFloat(selectMoney));
    }

    private void setSelectMoney(float money) {
        selectMoney = money;
        moneyBigDecimal = new BigDecimal(Float.toString(money));
    }

    private void checkSelect(InvoiceSelect select) {
        BigDecimal a = new BigDecimal(Float.toString(select.getMoney()));
        select.setSelect(!select.isSelect());
        if (select.isSelect()) {
            selectNum++;
            moneyBigDecimal = moneyBigDecimal.add(a);
        } else {
            selectNum--;
            moneyBigDecimal = moneyBigDecimal.subtract(a);
        }
        selectMoney = moneyBigDecimal.floatValue();
        setSelectText();
    }

    private void check() {
        if (cbAllSelect.isChecked()) {
            status = 1;
            invoice_ids = "";
        } else {
            StringBuffer buffer = new StringBuffer();
            for (InvoiceSelect select : invoiceSelects) {
                if (select.isSelect()) {
                    buffer.append(select.getBill_id());
                    buffer.append(',');
                }
            }
            if (buffer.length() <= 0) {
                showToast("您没有选择任何发票申请条目。");
                return;
            }
            status = 0;
            invoice_ids = buffer.substring(0, buffer.length() - 1);
        }
        AbstractRequest request = buildRequest(UrlUtils.invoiceCheck, RequestMethod.POST, null);
        request.add("status", status);
        request.add("invoice_ids", invoice_ids);
        executeNetwork(SAVE, "请稍后", request);
    }

    private void startInvoicePage(String data) {
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("invoice_ids", invoice_ids);
        bundle.putInt("status", status);
        startActivityForResult(SellerInvoiceActivity.class, bundle, RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT && resultCode == Activity.RESULT_OK) {
            initSelect();
            requestNetwork(GET);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.invoice_btn) {
            check();
        } else if (v.getId() == R.id.invoice_all_select) {
            if (cbAllSelect.isChecked()) {
                selectNum = num;
                setSelectMoney(total_fee);
            } else
                initSelect();
            setSelectText();
            allItemCheck(cbAllSelect.isChecked());
        }
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
        } else if (what == SAVE) {
            final JSONObject data = JSON.parseObject(result.getData().toString());
            int tip = data.getInteger("tip");
            if (tip == 1)
                dm.showAlertDialog2(result.getMsg(), "取消", "确定", new DialogManager.Callback() {
                    @Override
                    public void handleRight() {
                        startInvoicePage(data.toString());
                    }
                });
            else if (tip == 0)
                startInvoicePage(data.toString());
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
}
