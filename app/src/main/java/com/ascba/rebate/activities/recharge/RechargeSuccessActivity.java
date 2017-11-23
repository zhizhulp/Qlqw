package com.ascba.rebate.activities.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.mine.BalanceActivity;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;

/**
 * Created by 李平 on 2017/9/13 11:46
 * Describe:充值成功
 */

public class RechargeSuccessActivity extends BaseDefaultNetActivity {

    private TextView tvType;
    private TextView tvMoney;

    @Override
    protected int bindLayout() {
        return R.layout.activity_recharge_success;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        tvType = fv(R.id.tv_pay_type);
        tvMoney = fv(R.id.tv_pay_money);

        getParams();
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp(){
            @Override
            public void clickBack(View back) {
                backResults();
            }
        });
    }

    private void backResults() {
//        setResult(RESULT_OK,getIntent());
        startActivity(BalanceActivity.class,null);
        finish();
    }

    private void getParams() {
        Intent intent = getIntent();
        tvType.setText(intent.getStringExtra("type"));
        tvMoney.setText(intent.getStringExtra("money"));
    }

    @Override
    public void onBackPressed() {
        backResults();
    }
    //点击完成按钮
    public void complete(View view) {
        backResults();
    }
}
