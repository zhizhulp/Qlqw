package com.ascba.rebate.activities.trade;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.recharge.RechargeActivity;
import com.ascba.rebate.activities.setting.ForgetPayPwdActivity;
import com.ascba.rebate.activities.setting.SetPayPwdActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.Trade;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.EncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextHint;
import com.ascba.rebate.view.PsdDialog;
import com.ascba.rebate.view.picasso.CropCircleTransformation;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

public class OfflinePayActivity extends BaseDefaultNetActivity implements View.OnClickListener, TextWatcher {

    private RadioButton rbBlc;
    private RadioButton rbCash;
    private int payType = 2;//2余额 1现金
    private ImageView imIcon;
    private TextView tvName;
    private EditTextHint etMoney;
    private TextView tvBalance;
    private View tvPay;
    private String sellerID;//商户id
    private PsdDialog dialog;

    @Override
    protected int bindLayout() {
        return R.layout.activity_offline_pay;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        View latBalance = fv(R.id.lat_balance);
        View latCash = fv(R.id.lat_cash);
        rbBlc = fv(R.id.rb_balance);
        rbCash = fv(R.id.rb_cash);

        latBalance.setOnClickListener(this);
        latCash.setOnClickListener(this);
        rbBlc.setOnClickListener(this);
        rbCash.setOnClickListener(this);

        imIcon = fv(R.id.im_icon);
        tvName = fv(R.id.tv_name);
        etMoney = fv(R.id.et_money);
        tvBalance = fv(R.id.tv_balance);
        tvPay = fv(R.id.tv_pay);
        tvPay.setOnClickListener(this);
        etMoney.addTextChangedListener(this);
        etMoney.setFilters(new InputFilter[]{new InputFilter() {
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
        getParams();
    }

    private void getParams() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            sellerID = extras.getString("seller_id");
            tvName.setText(extras.getString("seller_name"));
            Picasso.with(this).load(extras.getString("seller_cover_logo"))
                    .transform(new CropCircleTransformation())
                    .placeholder(R.mipmap.head_loading).into(imIcon);
            tvBalance.setText("账户余额" + extras.getString("self_money"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lat_balance:
                payType = 2;
                rbBlc.setChecked(true);
                rbCash.setChecked(false);
                break;
            case R.id.lat_cash:
                payType = 1;
                rbCash.setChecked(true);
                rbBlc.setChecked(false);
                break;
            case R.id.rb_balance:
                payType = 2;
                rbBlc.setChecked(true);
                rbCash.setChecked(false);
                break;
            case R.id.rb_cash:
                payType = 1;
                rbCash.setChecked(true);
                rbBlc.setChecked(false);
                break;
            case R.id.tv_pay:
                if (payType == 2) {//余额支付
                    if (AppConfig.getInstance().getInt("is_level_pwd", 0) != 0) {
                        showPsdDialog();
                    } else {
                        dm.showAlertDialog2("请先设置支付密码", "取消", "设置密码", new DialogManager.Callback() {
                            @Override
                            public void handleRight() {
                                startActivity(SetPayPwdActivity.class, null);
                            }
                        });
                    }
                } else if (payType == 1) {//现金支付
                    requestApply(null);
                }

                break;
        }
    }

    private void showPsdDialog() {
        if (dialog == null) {
            dialog = new PsdDialog(this, R.style.dialog, new PsdDialog.OnPasswordInput() {
                @Override
                public void inputFinish(String number) {
                    dialog.dismiss();
                    requestApply(EncodeUtils.getPayPsd(number));
                }

                @Override
                public void inputCancel() {
                    dialog.dismiss();
                }
            });
        }
        dialog.showMyDialog();
    }

    private void requestApply(String password) {
        AbstractRequest request = buildRequest(UrlUtils.submit, RequestMethod.POST, null);
        request.add("seller", sellerID);
        request.add("money", etMoney.getText().toString());
        request.add("pay_type", payType);
        request.add("pay_password", password);
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            showToast(result.getMsg());
            String msg = (String) result.getData();
            JSONObject jObj = JSON.parseObject(msg);
            String info = jObj.getString("info");
            Trade trade = JSON.parseObject(info, Trade.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("trade",trade);
            startActivity(TradeConfirmActivity.class, bundle);
            finish();
        }
    }

    @Override
    protected void mHandle404(int what, Result result) {
        if (what == 0) {
            String data = (String) result.getData();
            JSONObject dataObj = JSON.parseObject(data);
            int errorStatus = dataObj.getIntValue("error_status");
            if (errorStatus == 1) {//余额不足
                dm.showAlertDialog2(result.getMsg(), "重新选择", "立即充值", new DialogManager.Callback() {
                    @Override
                    public void handleRight() {
                        startActivity(RechargeActivity.class, null);
                    }
                });
            } else if (errorStatus == 2) {//密码错误
                dm.showAlertDialog2(result.getMsg(), "重新输入", "忘记密码", new DialogManager.Callback() {
                    @Override
                    public void handleLeft() {
                        super.handleLeft();
                        dialog.showMyDialog();
                    }

                    @Override
                    public void handleRight() {
                        startActivity(ForgetPayPwdActivity.class, null);
                    }
                });
            } else if (errorStatus == 3) {//未设置支付密码
                super.mHandle404(what, result);
            } else if (errorStatus == 4) {//商家余额不足
                super.mHandle404(what, result);
            } else {
                super.mHandle404(what, result);
            }
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
        String s1 = s.toString();
        try {
            float v = Float.parseFloat(s1);
            if (v > 0) {
                tvPay.setEnabled(true);
            } else {
                tvPay.setEnabled(false);
            }
        } catch (NumberFormatException e) {
            tvPay.setEnabled(false);
        }
    }
}
