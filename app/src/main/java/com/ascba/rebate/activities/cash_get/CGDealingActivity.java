package com.ascba.rebate.activities.cash_get;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.mine.BalanceActivity;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.StepBean;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.timeline.HorizontalStepView;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 李平 on 2017/9/14 14:13
 * Describe:提现处理界面
 */

public class CGDealingActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private HorizontalStepView stepView;
    private Button btnOver;
    private int type;//0从提现进来 1从账单进来
    private TextView tvStatus;
    private TextView tvTitle;

    @Override
    protected int bindLayout() {
        return R.layout.activity_cgdealing;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        btnOver = fv(R.id.btn_complete);
        btnOver.setOnClickListener(this);
        getParams();
        stepView = fv(R.id.step_view);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickBack(View back) {
                if (type == 1) {
                    super.clickBack(back);
                } else {
                    backResults();
                }
            }
        });

        tvStatus = fv(R.id.tv_status);
        tvTitle = fv(R.id.tv_title);

    }

    private void getParams() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        requestData(extras.getString("till_id"));
        type = extras.getInt("type");
        if (type == 1) {
            btnOver.setVisibility(View.GONE);
        }
    }

    private void requestData(String tillId) {
        AbstractRequest request = buildRequest(UrlUtils.cashresult, RequestMethod.GET, null);
        request.add("bill_id", tillId);
        executeNetwork(0, "请稍后", request);
    }

    private void initStepView(String finishTime, String startTime, int status) {
        List<StepBean> stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("申请成功\n" + startTime, 1);
        StepBean stepBean1 = new StepBean("银行处理中\n" + startTime, 1);
        StepBean stepBean2 = new StepBean("提现成功\n" + finishTime, status == 2 ? 1 : -1);
        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);
        stepView.setStepViewTexts(stepsBeanList);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            String data = (String) result.getData();
            try {
                JSONObject jObj = new JSONObject(data);
                int status = jObj.optInt("status");
                initStepView(jObj.optString("finish_time"), jObj.optString("create_time"), status);
                tvStatus.setText(jObj.optString("till_tip"));
                tvTitle.setText(jObj.optString("till_title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (type == 0) {
            backResults();
        }
    }

    @Override
    public void onBackPressed() {
        if (type == 0) {
            backResults();
        } else {
            super.onBackPressed();
        }
    }

    private void backResults() {
//        setResult(RESULT_OK, getIntent());
        startActivity(BalanceActivity.class,null);
        finish();
    }
}
