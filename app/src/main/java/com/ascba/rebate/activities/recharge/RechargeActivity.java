package com.ascba.rebate.activities.recharge;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.BillActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultPayActivity;
import com.ascba.rebate.bean.Pay;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextHint;
import com.ascba.rebate.view.PaySelectDialog;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by 李平 on 2017/9/12 18:02
 * Describe: 充值-输入金额
 */

public class RechargeActivity extends BaseDefaultPayActivity implements View.OnClickListener, TextWatcher, PaySelectDialog.Callback {

    private Button btnPay;
    private EditTextHint etInput;

    private TextView tvAttention;
    private float maxMoney;

    @Override
    protected int bindLayout() {
        return R.layout.activity_recharge;
    }

    @Override
    protected Class<?> bindPaySuccessPage() {
        return RechargeSuccessActivity.class;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        btnPay = fv(R.id.btn_pay);
        btnPay.setOnClickListener(this);

        tvAttention = fv(R.id.tv_attention);

        etInput = fv(R.id.et_input);
        etInput.addTextChangedListener(this);
        // 添加输入过滤器
        etInput.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int length = dest.toString().substring(index).length();
                    if (length == 3) {
                        return "";
                    }
                }
                return null;
            }
        }});

        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                Bundle b = new Bundle();
                b.putInt("mine_type", 2);
                startActivity(BillActivity.class, b);
            }
        });
        requestData();
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.indexRecharge, RequestMethod.GET, null);
        executeNetwork(2, request);
    }

    @Override
    public void onClick(View v) {
        String input = etInput.getText().toString();
        try {
            float moy = Float.parseFloat(input);
            if (moy < 0.01f) {
                showToast("请输入有效金额");
                return;
            } else if (moy > maxMoney) {
                showToast("单笔金额最多可充值" + maxMoney + "元");
                return;
            }
        } catch (NumberFormatException e) {
            showToast("请输入有效金额");
            return;
        }
        showPayDialog(etInput.getText().toString(),null);
    }

    @Override
    protected void requestPayInfo(String type, String money, int what) {
        AbstractRequest request = buildRequest(UrlUtils.payment, RequestMethod.POST, Pay.class);
        request.add("pay_type", type);
        request.add("total_fee", money);
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 2) {
            String data = (String) result.getData();
            JSONObject jObj = JSON.parseObject(data);
            AppConfig.getInstance().putInt("is_level_pwd", jObj.getIntValue("is_level_pwd"));
            maxMoney = jObj.getFloat("max_money");

            tvAttention.setText("单笔金额最多可充值" + maxMoney + "元");
            tvAttention.setTextColor(getResources().getColor(R.color.grey_black_tv));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String input = s.toString();
        if (s.length() > 0) {
            try {
                float moy = Float.parseFloat(input);
                if (moy > maxMoney) {
                    tvAttention.setText("充值金额超限");
                    tvAttention.setTextColor(getResources().getColor(R.color.red));
                    btnPay.setEnabled(false);
                } else {
                    tvAttention.setText("单笔金额最多可充值" + maxMoney + "元");
                    tvAttention.setTextColor(getResources().getColor(R.color.grey_black_tv));
                    btnPay.setEnabled(true);
                }
            } catch (NumberFormatException e) {
                tvAttention.setText("单笔金额最多可充值" + maxMoney + "元");
                tvAttention.setTextColor(getResources().getColor(R.color.grey_black_tv));
                btnPay.setEnabled(true);
            }
        } else {
            tvAttention.setText("单笔金额最多可充值" + maxMoney + "元");
            tvAttention.setTextColor(getResources().getColor(R.color.grey_black_tv));
            btnPay.setEnabled(false);
        }
    }
}
