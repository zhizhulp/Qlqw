package com.ascba.rebate.activities.success;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.merchant.MctApplyStartActivity;
import com.ascba.rebate.activities.score_shop.GiftExchangeLogActivity;
import com.ascba.rebate.activities.seller.SellerInvoiceHistoryActivity;
import com.ascba.rebate.activities.trade.ReceiveCodeActivity;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;

public class TextInfoSuccessActivity extends BaseDefaultNetActivity {

    private TextView tvType;
    private TextView tvMoney;
    private TextView tvTitle;
    private Button btnComplete;
    private View wave;
    int type;
    int select = 0;

    @Override
    protected int bindLayout() {
        return R.layout.activity_gift_exchange_success;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        tvType = fv(R.id.tv_pay_type);
        tvMoney = fv(R.id.tv_pay_money);
        tvTitle = fv(R.id.tv_title);
        btnComplete = fv(R.id.btn_complete);
        wave = fv(R.id.wave_line);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) wave.getLayoutParams();
        params.height = getResources().getDisplayMetrics().widthPixels / 18;
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
                if (type == 1)
                    startActivity(SellerInvoiceHistoryActivity.class, null);
                else if (type == 2)
                    GiftExchangeLogActivity.jumpIntent(TextInfoSuccessActivity.this, select);
                else if (type == 3)
                    if (select == 1) startActivity(MctApplyStartActivity.class, null);
                    else startActivity(ReceiveCodeActivity.class, null);
                finish();
            }
        });
    }

    private void backResults() {
        finish();
    }

    private void getParams() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        if (type == 1) {
            mMoneyBar.setTextTitle("申请成功");
            tvTitle.setText("申请成功");
            btnComplete.setText("查看申请");
            tvMoney.setText("\u3000\u3000" + intent.getStringExtra("info"));
        } else if (type == 2) {
            select = intent.getIntExtra("select", 0);
            mMoneyBar.setTextTitle("兑换成功");
            tvTitle.setText("兑换成功");
            btnComplete.setText("立即查看");
            tvMoney.setText("\u3000\u3000" + intent.getStringExtra("info"));
        } else if (type == 3) {
            mMoneyBar.setTextTitle("支付成功");
            tvTitle.setText("支付成功");
            select = intent.getIntExtra("select", 0);
            if (select == 1)
                btnComplete.setText("完善资料");
            else
                btnComplete.setText("立即体验");
            tvMoney.setText("\u3000\u3000" + intent.getStringExtra("info"));
        }
    }

    @Override
    public void onBackPressed() {
        backResults();
    }
}
