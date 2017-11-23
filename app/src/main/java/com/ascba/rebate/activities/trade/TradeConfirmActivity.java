package com.ascba.rebate.activities.trade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.Trade;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.picasso.CropCircleTransformation;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by 李平 on 2017/10/14 15:00
 * Describe:确认订单
 */

public class TradeConfirmActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private ImageView imHead;
    private TextView tvNickName;
    private TextView tvPayType;
    private TextView tvEmployee;
    private TextView tvTime;
    private TextView tvOrderNum;
    private TextView tvCost;
    private TextView tvStatus;
    private String orderId;
    private View tvCancel;
    private View tvSure;
    private int resultCode = RESULT_CANCELED;
    private TextView tvUserName;
    private TextView tvBusName;
    private TextView tvUserScore;
    private TextView tvBusScore;
    private View latUserName;
    private View latBusName;
    private View latUserScore;
    private View latBusScore;
    private View latEmployee;
    private TextView tvBusToUser;//赠送消费者
    private View latBusToUser;

    @Override
    protected int bindLayout() {
        return R.layout.activity_trade_confirm;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        imHead = fv(R.id.im_head);
        tvNickName = fv(R.id.tv_nick_name);
        tvCost = fv(R.id.tv_cost);
        tvStatus = fv(R.id.tv_status);
        tvPayType = fv(R.id.tv_payType);

        tvUserName = fv(R.id.tv_user_name);
        tvBusName = fv(R.id.tv_bus_name);

        tvUserScore = fv(R.id.tv_user_score);
        tvBusScore = fv(R.id.tv_bus_score);

        tvBusToUser = fv(R.id.tv_to_user);

        latUserName = fv(R.id.lat_user_name);
        latUserScore = fv(R.id.lat_user_score);

        latBusName = fv(R.id.lat_bus_name);
        latBusScore = fv(R.id.lat_bus_score);
        latEmployee = fv(R.id.lat_employee);
        latBusToUser = fv(R.id.lat_to_user);

        tvEmployee = fv(R.id.tv_employee);
        tvTime = fv(R.id.tv_time);
        tvOrderNum = fv(R.id.tv_order_num);

        tvCancel = fv(R.id.btn_cancel);
        tvSure = fv(R.id.btn_sure);
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        getParams();

        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickBack(View back) {
                backAndResults();
            }
        });
    }

    private void backAndResults() {
        setResult(resultCode, getIntent());
        finish();
    }

    @Override
    public void onBackPressed() {
        backAndResults();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dm.showAlertDialog2("确定要取消此笔订单吗?", null, null, new DialogManager.Callback() {
                    @Override
                    public void handleLeft() {
                        requestCancel();
                    }
                });
                break;
            case R.id.btn_sure:
                requestSure();
                break;
        }
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.info, RequestMethod.GET, null);
        request.add("order_id", orderId);
        executeNetwork(2, "请稍后", request);
    }

    private void requestSure() {
        AbstractRequest request = buildRequest(UrlUtils.affirm, RequestMethod.POST, null);
        request.add("order_id", orderId);
        executeNetwork(0, "请稍后", request);
    }

    private void requestCancel() {
        AbstractRequest request = buildRequest(UrlUtils.cancel, RequestMethod.POST, null);
        request.add("order_id", orderId);
        executeNetwork(1, "请稍后", request);
    }

    private void getParams() {
        Intent intent = getIntent();
        orderId = intent.getStringExtra("order_id");
        if (orderId != null) {
            requestData();
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Trade trade = extras.getParcelable("trade");
            setUI(trade, true);
        }

    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0 || what == 1) {
            resultCode = RESULT_OK;
            showToast(result.getMsg());
            tvSure.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
            if (what == 0) {
                tvStatus.setText("交易成功");
            } else {
                tvStatus.setText("交易取消");
            }
        } else if (what == 2) {
            String data = (String) result.getData();
            JSONObject jObj = JSON.parseObject(data);
            String info = jObj.getString("info");
            Trade trade = JSON.parseObject(info, Trade.class);
            setUI(trade, false);
        }

    }

    private void setUI(Trade trade, boolean type) {
        if (trade != null) {
            int orderStatus = trade.getOrder_status();
            if (!type) {
                if (orderStatus == 0) {//交易中
                    mMoneyBar.setTextTitle("订单确认");
                    tvSure.setVisibility(View.VISIBLE);
                    tvCancel.setVisibility(View.VISIBLE);
                } else {//交易取消或交易成功
                    mMoneyBar.setTextTitle("订单详情");
                    tvSure.setVisibility(View.GONE);
                    tvCancel.setVisibility(View.GONE);
                }
            } else {
                mMoneyBar.setTextTitle("订单详情");
            }
            Picasso.with(this).load(trade.getAvatar())
                    .transform(new CropCircleTransformation())
                    .placeholder(R.mipmap.head_loading).into(imHead);
            tvNickName.setText(trade.getName());
            tvCost.setText(trade.getMoney());
            tvStatus.setText(trade.getOrder_status_text());
            tvPayType.setText(trade.getPay_type_text());
            tvBusName.setText(trade.getMember_username());
            tvUserName.setText(trade.getMember_username());
            tvUserScore.setText(trade.getScore() + "");
            tvBusScore.setText(trade.getScore() + "");
            tvBusToUser.setText(trade.getScore() + "");
            tvEmployee.setText(trade.getPay_commission());
            tvTime.setText(trade.getCreate_time());
            tvOrderNum.setText(trade.getOrder_number());
            /**
             latUserName = fv(R.id.lat_user_name);
             latUserScore = fv(R.id.lat_user_score);

             latBusName = fv(R.id.lat_bus_name);
             latBusScore = fv(R.id.lat_bus_score);
             latEmployee = fv(R.id.lat_employee);
             latBusToUser = fv(R.id.lat_to_user);
             */
            String orderIdentity = trade.getOrder_identity();
            if (orderIdentity != null) {
                if (orderIdentity.equals("customer")) {//用户显示
                    latUserName.setVisibility(View.GONE);
                    latUserScore.setVisibility(View.VISIBLE);
                    latBusName.setVisibility(View.VISIBLE);
                    latBusScore.setVisibility(View.GONE);
                    latEmployee.setVisibility(View.GONE);
                    latBusToUser.setVisibility(View.GONE);
                } else {//商家显示
                    latUserName.setVisibility(View.VISIBLE);
                    latUserScore.setVisibility(View.GONE);
                    latBusName.setVisibility(View.GONE);
                    latBusScore.setVisibility(View.VISIBLE);
                    latEmployee.setVisibility(View.VISIBLE);
                    if (trade.isPurchase_status()) {
                        latBusToUser.setVisibility(View.GONE);
                    } else {
                        latBusToUser.setVisibility(View.VISIBLE);
                        tvBusScore.setText(trade.getSys_score_text());
                    }
                }
            } else {
                showToast("用户数据异常");
                finish();
            }
        }
    }
}
