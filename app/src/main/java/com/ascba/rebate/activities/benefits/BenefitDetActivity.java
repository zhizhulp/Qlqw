package com.ascba.rebate.activities.benefits;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.BenDet;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by 李平 on 2017/10/30 18:59
 * Describe:分红详情
 */

public class BenefitDetActivity extends BaseDefaultNetActivity {

    private TextView tvTotal;
    private TextView tvThisTotal;
    private TextView tvRate;
    private TextView tvCash;
    private TextView tvTicket;
    private TextView tvService;
    private TextView tvTime;
    private TextView tvOrderNum;
    private int resultCode = RESULT_CANCELED;
    private int id;

    @Override
    protected int bindLayout() {
        return R.layout.activity_benefit_det;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        tvTotal = fv(R.id.tv_total);
        tvThisTotal = fv(R.id.tv_this_total);
        tvRate = fv(R.id.tv_rate);
        tvCash = fv(R.id.tv_cash);
        tvTicket = fv(R.id.tv_ticket);
        tvService = fv(R.id.tv_service_fee);
        tvTime = fv(R.id.tv_time);
        tvOrderNum = fv(R.id.tv_order_num);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickBack(View back) {
                setResults();
            }
        });
        getParams();
        requestData();
    }
    private void setResults() {
        Intent intent = getIntent();
        setResult(resultCode, intent);
        finish();
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.CapitalProfitdetail, RequestMethod.GET, BenDet.class);
        request.add("log_id",id);
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            resultCode = RESULT_OK;
            BenDet data = (BenDet) result.getData();
            tvTotal.setText(data.getAll_score() + "");
            tvThisTotal.setText(data.getBonus_score());
            tvRate.setText(data.getBonus_pro());
            tvCash.setText(data.getBonus_money());
            tvTicket.setText(data.getBonus_voucher());
            tvService.setText("本次寄卖服务费" + data.getBonus_rate());
            tvTime.setText(data.getBonus_time());
            tvOrderNum.setText(data.getBonus_order_sn());
        }
    }
    private void getParams() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
    }

    public static void jumpIntent(Context context, int id) {
        Intent intent = new Intent(context, BenefitDetActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
    public static void jumpResultIntent(Context context, int id) {
        Intent intent = new Intent(context, BenefitDetActivity.class);
        intent.putExtra("id", id);
        ((Activity) context).startActivityForResult(intent, CodeUtils.REQUEST_CLEAR);
    }

    @Override
    protected void mHandle404(int what, Result result) {
        if (what == 0) {
            super.mHandle404(what, result);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResults();
    }
}
