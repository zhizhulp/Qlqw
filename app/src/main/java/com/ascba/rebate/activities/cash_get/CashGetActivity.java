package com.ascba.rebate.activities.cash_get;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.bank_card.AddBankCardActivity;
import com.ascba.rebate.activities.bill.BillActivity;
import com.ascba.rebate.activities.setting.SetPayPwdActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.BankCard;
import com.ascba.rebate.bean.CashGet;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.EncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextHint;
import com.ascba.rebate.view.PsdDialog;
import com.ascba.rebate.view.SelBCardDialog;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2017/9/13 18:37
 * Describe: 提现
 */

public class CashGetActivity extends BaseDefaultNetActivity implements View.OnClickListener, SelBCardDialog.Callback, TextWatcher {
    private List<BankCard.BankListBean> data;
    private SelBCardDialog dialog;
    private View viewEmpty;
    private View viewMsg;
    private TextView tvName;
    private TextView tvType;
    private EditTextHint etMoney;
    private TextView tvStatus;
    private Button btnApply;
    private TextView tvSerFee;
    private String money;
    private String timeTo;
    private int selBankId = -1000;//选择的银行卡id
    private PsdDialog psdDialog;
    private boolean hasBanks;//是否有银行卡

    @Override
    protected int bindLayout() {
        return R.layout.activity_cash_get;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        data = new ArrayList<>();
        fv(R.id.lat_sel_card).setOnClickListener(this);
        viewEmpty = fv(R.id.lat_empty);
        viewMsg = fv(R.id.lat_card_msg);
        tvName = fv(R.id.tv_name);
        tvType = fv(R.id.tv_type);
        etMoney = fv(R.id.et_money);
        tvStatus = fv(R.id.tv_status);
        tvSerFee = fv(R.id.tv_service_fee);
        btnApply = fv(R.id.btn_apply);
        btnApply.setOnClickListener(this);
        etMoney.addTextChangedListener(this);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                Bundle b = new Bundle();
                b.putInt("mine_type", 1);
                startActivity(BillActivity.class, b);
            }
        });
        requestData();
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.indexCash, RequestMethod.GET, CashGet.class);
        executeNetwork(0, "请稍后", request);
    }

    private void requestCashGet() {
        AbstractRequest request = buildRequest(UrlUtils.tillsMoney, RequestMethod.POST, null);
        request.add("money", etMoney.getText().toString());
        request.add("bank_id", selBankId);
        executeNetwork(1, "请稍后", request);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            CashGet cashGet = (CashGet) result.getData();
            List<BankCard.BankListBean> banks = cashGet.getBank_info();
            this.money = cashGet.getMoney();
            this.timeTo = cashGet.getCash_intup_tip();
            btnApply.setText(cashGet.getCash_button_tip());
            tvSerFee.setText("提现金额" + "(" + cashGet.getTake_money_rate() + ")");
            tvStatus.setText("可提现金额" + money);
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.grey_black_tv2));
            if (banks != null && banks.size() > 0) {
                hasBanks = true;
                data.clear();
                data.addAll(banks);
                tvName.setText(banks.get(0).getBank());
                this.selBankId = banks.get(0).getId();
                tvType.setText("**** **** **** " + banks.get(0).getBank_card() + " " + banks.get(0).getNature());
                viewEmpty.setVisibility(View.GONE);
                viewMsg.setVisibility(View.VISIBLE);
            } else {
                hasBanks = false;
                viewEmpty.setVisibility(View.VISIBLE);
                viewMsg.setVisibility(View.GONE);
            }
        } else if (what == 1) {
            String data = (String) result.getData();
            try {
                JSONObject jObj = new JSONObject(data);
                String tillId = jObj.optString("bill_id");
                Bundle r = new Bundle();
                r.putString("till_id", tillId);
//                startActivityForResult(CGDealingActivity.class, r, CodeUtils.REQUEST_CASH_GET);
                startActivity(CGDealingActivity.class, r);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (what == 2) {
            requestCashGet();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lat_sel_card:
                if (viewMsg.getVisibility() == View.VISIBLE) {
                    if (dialog == null) {
                        dialog = new SelBCardDialog(this, data);
                        dialog.setCallback(this);
                    }
                    dialog.show();
                } else {
                    startActivityForResult(AddBankCardActivity.class, null, CodeUtils.REQUEST_ADD_CARD);
                }

                break;
            case R.id.btn_apply:
                if (hasBanks) {
                    if (AppConfig.getInstance().getInt("is_level_pwd", 0) == 0) {
                        dm.showAlertDialog2(getString(R.string.no_pay_psd), null, null, new DialogManager.Callback() {
                            @Override
                            public void handleLeft() {
                                startActivity(SetPayPwdActivity.class, null);
                            }
                        });
                    } else {
                        String input = etMoney.getText().toString();
                        try {
                            float moy = Float.parseFloat(input);
                            if (moy > Float.parseFloat(money)) {
                                showToast("您输入的金额超过可提现金额。");
                                return;
                            }
                            if (moy == 0 || moy % 100 != 0) {
                                showToast("输入金额必须是100的整数倍");
                                return;
                            }
                        } catch (NumberFormatException e) {
                            showToast("请输入有效金额");
                            return;
                        }
                        showPsdDialog();

                    }
                } else {
                    showToast("请绑定银行卡");
                }
                break;
        }
    }

    private void showPsdDialog() {
        if (psdDialog == null) {
            psdDialog = new PsdDialog(CashGetActivity.this, R.style.dialog, new PsdDialog.OnPasswordInput() {
                @Override
                public void inputFinish(String number) {
                    requestCheckPayParams(EncodeUtils.getPayPsd(number));
                }

                @Override
                public void inputCancel() {
                    psdDialog.dismiss();
                }
            });
        }
        psdDialog.showMyDialog();
    }

    private void requestCheckPayParams(String payPsd) {
        AbstractRequest request = buildRequest(UrlUtils.checkPayPassword, RequestMethod.POST, null);
        request.add("level_pwd", payPsd);
        executeNetwork(2, request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.REQUEST_ADD_CARD && resultCode == RESULT_OK) {
            requestData();
//        } else if (requestCode == CodeUtils.REQUEST_CASH_GET && resultCode == RESULT_OK) {
//            setResult(RESULT_OK);
//            finish();
        } else if (requestCode == 0 && resultCode == RESULT_OK) {
            requestData();
        }
    }

    @Override
    public void onCancel() {
        dialog.dismiss();
    }

    @Override
    public void onSure(BankCard.BankListBean bankCard) {
        Log.d(TAG, "onSure: ");
        selBankId = bankCard.getId();
        tvName.setText(bankCard.getBank());
        tvType.setText("**** **** **** " + bankCard.getBank_card() + " " + bankCard.getNature());
        dialog.dismiss();
    }

    @Override
    public void onAddCard() {
        startActivity(AddBankCardActivity.class, null);
        dialog.dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            btnApply.setEnabled(true);
            float fm = Float.parseFloat(money);
            String sm = etMoney.getText().toString();
            try {
                float em = Float.parseFloat(sm);
                if (em > fm) {
                    btnApply.setEnabled(false);
                    tvStatus.setText("您输入的金额超过可提现金额。");
                    tvStatus.setTextColor(ContextCompat.getColor(this, R.color.red));
                } else {
                    tvStatus.setText(timeTo);
                    tvStatus.setTextColor(ContextCompat.getColor(this, R.color.grey_black_tv2));
                }
            } catch (NumberFormatException e) {
                tvStatus.setText("可提现金额" + money);
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.grey_black_tv2));
                showToast(getString(R.string.input_money_unavailable));
            }

        } else {
            btnApply.setEnabled(false);
            tvStatus.setText("可提现金额" + money);
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.grey_black_tv2));
        }
    }

}
