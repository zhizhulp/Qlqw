package com.ascba.rebate.activities.seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.utils.PayUtils;

public class PurchaseSuccessActivity extends BaseDefaultNetActivity {

    private TextView tvType;
    private TextView tvMoney;
    private TextView tvTitle;
    private Button btnComplete;
    int type;

    @Override
    protected int bindLayout() {
        return R.layout.activity_seller_purchase_success;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        tvType = fv(R.id.tv_pay_type);
        tvMoney = fv(R.id.tv_pay_money);
        tvTitle = fv(R.id.tv_title);
        btnComplete = fv(R.id.btn_complete);

        getParams();
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickBack(View back) {
                backResults();
            }
        });
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0)
                    startActivity(SellerGiveCreateActivity.class, null);
                finish();
            }
        });
    }

    private void backResults() {
        if (type == 0)
            startActivity(SellerPurchaseActivity.class, null);
        finish();
    }

    private void getParams() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        if (type == 0) {
            mMoneyBar.setTextTitle("购买成功");
            tvTitle.setText("购买成功");
            btnComplete.setText("立即赠送");
            tvMoney.setText(PayUtils.getInstance().info);
        } else if (type == 1) {
            mMoneyBar.setTextTitle("赠送成功");
            tvTitle.setText("赠送成功");
            btnComplete.setText("完成");
            tvMoney.setText(intent.getStringExtra("info"));
        }
    }

    @Override
    public void onBackPressed() {
        backResults();
    }
}
