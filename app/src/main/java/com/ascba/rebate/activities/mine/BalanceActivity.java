package com.ascba.rebate.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.bank_card.BankCardListActivity;
import com.ascba.rebate.activities.bill.BillActivity;
import com.ascba.rebate.activities.cash_get.CashGetActivity;
import com.ascba.rebate.activities.personal_identification.PIStartActivity;
import com.ascba.rebate.activities.recharge.RechargeActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.Balance;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * 余额
 */
public class BalanceActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private TextView tvBalance;
    private int resultCode = RESULT_CANCELED;
    private String url;

    @Override
    protected int bindLayout() {
        return R.layout.activity_remainder;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                startActivity(BillActivity.class, null);
            }

            @Override
            public void clickBack(View back) {
                backResults();
            }
        });

        fv(R.id.lat_recharge).setOnClickListener(this);
        fv(R.id.lat_cash_get).setOnClickListener(this);
        fv(R.id.lat_bank_card).setOnClickListener(this);
        fv(R.id.tv_problems).setOnClickListener(this);

        tvBalance = fv(R.id.tv_balance);

        requestNetwork();
    }

    private void backResults() {
        setResult(resultCode, getIntent());
        finish();
    }

    private void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.index, RequestMethod.GET, Balance.class);
        executeNetwork(0, request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lat_recharge:
                startActivityForResult(RechargeActivity.class, null, CodeUtils.REQUEST_RECHARGE);
                break;
            case R.id.lat_cash_get:
                if (AppConfig.getInstance().getInt("card_status", 0) == 0) {
                    dm.showAlertDialog2(getString(R.string.no_pi), null, null, new DialogManager.Callback() {
                        @Override
                        public void handleLeft() {
                            startActivity(PIStartActivity.class, null);
                        }
                    });
                } else {
                    startActivityForResult(CashGetActivity.class, null, CodeUtils.REQUEST_CASH_GET);
                }
                break;
            case R.id.lat_bank_card:
                startActivity(BankCardListActivity.class, null);
                break;
            case R.id.tv_problems:
                Intent intent = new Intent(this, WebViewBaseActivity.class);
                intent.putExtra("name", "常见问题");
                intent.putExtra("url", url);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            Balance balance = (Balance) result.getData();
            tvBalance.setText(balance.getMoney());
            this.url = balance.getBalance_question();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.REQUEST_RECHARGE && resultCode == RESULT_OK) {
            this.resultCode = RESULT_OK;
            requestNetwork();
        } else if (requestCode == CodeUtils.REQUEST_CASH_GET && resultCode == RESULT_OK) {
            this.resultCode = RESULT_OK;
            requestNetwork();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent: ");
        requestNetwork();
    }

    @Override
    public void onBackPressed() {
        backResults();
    }
}
