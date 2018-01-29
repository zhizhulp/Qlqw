package com.ascba.rebate.activities.score_buy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.success.TextInfoSuccessActivity;
import com.ascba.rebate.adapter.PurchasePayAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultPayActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.Pay;
import com.ascba.rebate.bean.PurchaseEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.NumberFormatUtils;
import com.ascba.rebate.utils.PayUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yanzhenjie.nohttp.RequestMethod;

import java.math.BigDecimal;

/**
 * Created by Jero on 2017/10/16 0016.
 */

public class PurchaseActivity extends BaseDefaultPayActivity implements View.OnClickListener {
    private static final int GET = 80;

    private int type; // 1 零售 2 批发 3 定制
    private PurchaseEntity purchaseEntity;
    private PurchasePayAdapter purchasePayAdapter;
    // foot
    private View footerView;
    private CheckBox cbBack, cbInvoice;
    private TextView tvBack, tvInvoice, tvBackContent, tvInvoiceContent;
    private RelativeLayout latInvoice; // 选择发票布局
    // car
    private RelativeLayout latCar; // 购物车数字区域
    private ImageView imCarNum; // 红点数字
    private LinearLayout latCarShow; // 购物车展开布局
    private TextView tvCarInfo, tvCarNum, tvCarName, tvCarMoney, tvCarBack, tvCarBackMoney, tvCarInvoice, tvCarInvoiceMoney;
    private Button btnOk;
    private View vLine; // 购物车灰线
    private int num = 0; // 购物车数量
    private String money; // 总金额
    private Float invoiceNum; // 发票金额

    @Override
    protected int bindLayout() {
        return R.layout.activity_seller_purchase;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        getParams();
        btnOk = fv(R.id.seller_purchase_ok);
        btnOk.setOnClickListener(this);
        fv(R.id.seller_purchase_url).setOnClickListener(this);
        tvCarNum = fv(R.id.seller_purchase_num);
        tvCarInfo = fv(R.id.seller_purchase_info);
        latCarShow = fv(R.id.lat_car_show);
        latCarShow.setOnClickListener(this);
        imCarNum = fv(R.id.purchase_car_num);
        latCar = fv(R.id.lat_car);
        latCar.setOnClickListener(this);
        tvCarName = fv(R.id.purchase_car_name);
        tvCarMoney = fv(R.id.purchase_car_money);
        tvCarBack = fv(R.id.purchase_car_back);
        tvCarBackMoney = fv(R.id.purchase_car_back_money);
        tvCarInvoice = fv(R.id.purchase_car_invoice);
        tvCarInvoiceMoney = fv(R.id.purchase_car_invoice_money);
        vLine = fv(R.id.v_line);
        purchasePayAdapter = new PurchasePayAdapter();
        mRecyclerView.setAdapter(purchasePayAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        purchasePayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                purchasePayAdapter.setSelect(position);
                setSelectCar();
            }
        });
        requestNetwork();
    }

    private void getParams() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getInt("type");
            switch (type) {
                case 1:
                    mMoneyBar.setTextTitle("零售");
                    break;
                case 2:
                    mMoneyBar.setTextTitle("批发");
                    break;
                case 3:
                    mMoneyBar.setTextTitle("定制");
                    break;
            }
        } else {
            mMoneyBar.setTextTitle("礼品采购");
        }
    }

    private void setFooterView() {
        if (footerView == null) {
            footerView = getLayoutInflater().from(this).inflate(R.layout.item_purchase_foot, null);
            cbBack = footerView.findViewById(R.id.cb_select);
            cbInvoice = footerView.findViewById(R.id.cb_invoice_select);
            cbBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setBackSelect(isChecked);
                }
            });
            cbInvoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setInvoiceSelect(isChecked);
                }
            });
            tvBack = footerView.findViewById(R.id.tv_title);
            tvInvoice = footerView.findViewById(R.id.tv_invoice_title);
            tvBackContent = footerView.findViewById(R.id.tv_content);
            tvInvoiceContent = footerView.findViewById(R.id.tv_invoice_content);
            footerView.findViewById(R.id.im_web).setOnClickListener(this);
            footerView.findViewById(R.id.im_invoice_web).setOnClickListener(this);
            latInvoice = footerView.findViewById(R.id.lat_invoice);
            purchasePayAdapter.addFooterView(footerView);
        }
        tvBack.setText(purchaseEntity.getBack_money_text());
        tvInvoice.setText(purchaseEntity.getInvoice_text());
        tvBackContent.setText(purchaseEntity.getBack_money_tips());
        tvInvoiceContent.setText(purchaseEntity.getInvoice_tips());
        latInvoice.setVisibility(purchaseEntity.getIs_invoice() == 1 ? View.VISIBLE : View.GONE);
    }

    private void initCarView() {
        num = 0;
        tvCarNum.setText("0");
        imCarNum.setImageResource(0);
        btnOk.setEnabled(false);
        latCar.setEnabled(false);
        tvCarInfo.setVisibility(View.GONE);
    }

    private void resetCarView() {
        setNum(1);
        setSelectCar();
        btnOk.setEnabled(true);
        latCar.setEnabled(true);
        cbBack.setChecked(true);
        cbInvoice.setChecked(purchaseEntity.getIs_invoice() == 1);
        tvCarBack.setText(purchaseEntity.getBack_money_text());
        tvCarInvoice.setText(purchaseEntity.getInvoice_text());
        tvCarBackMoney.setText("￥" + purchaseEntity.getBack_money());
    }

    private void setSelectCar() {
        PurchaseEntity.MoneyConfigBean item = purchasePayAdapter.getSelectItem();
        if (item.getOnly_price() == 1)
            tvCarName.setText(item.getCz_desc());
        else if (item.getOnly_price() == 0)
            tvCarName.setText(item.getCz_desc() + "（" + item.getCz_corner() + "）");
        tvCarMoney.setText("￥" + item.getCz_money());
        invoiceNum = Float.parseFloat(item.getCz_money()) * purchaseEntity.getInvoice() / 100;
        tvCarInvoiceMoney.setText("￥" + NumberFormatUtils.getNewFloat(invoiceNum));
        setMoney();
    }

    private void setMoney() {
        float mm = Float.parseFloat(purchasePayAdapter.getSelectItem().getCz_money());
        if (cbBack.isChecked())
            mm += purchaseEntity.getBack_money();
        if (cbInvoice.isChecked())
            mm += invoiceNum;
        money = NumberFormatUtils.getNewFloat(mm);
        tvCarNum.setText(money);
    }

    private void setBackSelect(boolean select) {
        if (select) {
            tvCarBack.setVisibility(View.VISIBLE);
            tvCarBackMoney.setVisibility(View.VISIBLE);
            setNum(++num);
        } else {
            tvCarBack.setVisibility(View.GONE);
            tvCarBackMoney.setVisibility(View.GONE);
            setNum(--num);
        }
        setMoney();
    }

    private void setInvoiceSelect(boolean select) {
        if (select) {
            tvCarInvoice.setVisibility(View.VISIBLE);
            tvCarInvoiceMoney.setVisibility(View.VISIBLE);
            setNum(++num);
        } else {
            tvCarInvoice.setVisibility(View.GONE);
            tvCarInvoiceMoney.setVisibility(View.GONE);
            setNum(--num);
        }
        setMoney();
    }

    private void setNum(int num) {
        this.num = num;
        switch (num) {
            case 1:
                imCarNum.setImageResource(R.mipmap.purchase1);
                vLine.setVisibility(View.GONE);
                tvCarInfo.setVisibility(View.GONE);
                break;
            case 2:
                imCarNum.setImageResource(R.mipmap.purchase2);
                vLine.setVisibility(View.VISIBLE);
                tvCarInfo.setVisibility(View.VISIBLE);
                if (cbBack.isChecked())
                    tvCarInfo.setText("内含退款保障费用，可勾选/取消");
                else if (cbInvoice.isChecked())
                    tvCarInfo.setText("内含发票报销费用，可勾选/取消");
                break;
            case 3:
                imCarNum.setImageResource(R.mipmap.purchase3);
                tvCarInfo.setText("内含退款保障和发票报销费用，可勾选/取消");
                vLine.setVisibility(View.VISIBLE);
                tvCarInfo.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (purchaseEntity == null) {
            requestNetwork();
            return;
        }
        switch (v.getId()) {
            case R.id.seller_purchase_ok:
                payStart(money, purchaseEntity.getMoney());
                break;
            case R.id.seller_purchase_url:
                WebViewBaseActivity.start(this, "采购协议", purchaseEntity.getAgreement_url());
                break;
            case R.id.im_web:
                WebViewBaseActivity.start(this, purchaseEntity.getBack_money_text(), purchaseEntity.getBack_money_url());
                break;
            case R.id.im_invoice_web:
                WebViewBaseActivity.start(this, purchaseEntity.getInvoice_text(), purchaseEntity.getInvoice_url());
                break;
            case R.id.lat_car:
                if (num == 0)
                    return;
                latCarShow.setVisibility(latCarShow.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
            case R.id.lat_car_show:
                latCarShow.setVisibility(View.GONE);
                break;
        }
    }

    private void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.purchasePay, RequestMethod.GET, PurchaseEntity.class);
        request.add("cate", type);
        executeNetwork(GET, "请稍后", request);
    }

    @Override
    protected void requestPayInfo(String type, String money, int what) {
        AbstractRequest request = buildRequest(UrlUtils.purchasePayment, RequestMethod.POST, Pay.class);
        request.add("pay_type", type);
        request.add("level_id", purchasePayAdapter.getSelectItem().getLevel_id());
        request.add("has_back_money", cbBack.isChecked() ? 1 : 0);
        request.add("has_invoice", cbInvoice.isChecked() ? 1 : 0);
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == GET) {
            purchaseEntity = (PurchaseEntity) result.getData();
            purchasePayAdapter.setNewData(purchaseEntity.getMoney_config());
            if (purchaseEntity.getMoney_config().size() == 0) {
                initCarView();
                return;
            }
            AppConfig.getInstance().putInt("is_level_pwd", purchaseEntity.getIs_level_pwd());
            setFooterView();
            resetCarView();
        }
    }

    @Override
    public void payFinish(String type) {
        if (type.equals(PayUtils.BALANCE)) {
            BigDecimal balance, money;
            balance = new BigDecimal(purchaseEntity.getMoney());
            money = new BigDecimal(payUtils.money);
            balance = balance.subtract(money);
            purchaseEntity.setMoney(balance.toString());
        }
        payUtils.clear();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 5);
        bundle.putString("info", pay.getSuccess_info());
        startActivityForResult(TextInfoSuccessActivity.class, bundle, CodeUtils.REQUEST_PAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.REQUEST_PAY && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
