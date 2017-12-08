package com.ascba.rebate.view;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.PayTypeAdapter;
import com.ascba.rebate.bean.PayType;
import com.ascba.rebate.utils.PayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2017/9/13 10:39
 * Describe:选择支付方式
 */

public class PaySelectDialog extends BottomSheetDialog {
    private String price;
    private Callback callback;
    private String type;
    private TextView tvPrice;
    private String balance;

    private List<PayType> types;
    private PayTypeAdapter pt;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public PaySelectDialog(Context context, String price) {
        this(context, price, null);
    }

    public PaySelectDialog(Context context, String price, String balance) {
        super(context);
        this.price = price;
        this.balance = balance;
        init();
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setBalance(String price, String balance) {
        setPrice(price);
        this.balance = balance;
        initPayTypesData(types);
    }

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.layout_pay_pop, null);
        setContentView(contentView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        tvPrice = ((TextView) findViewById(R.id.dlg_tv_total_cash));
        //关闭对话框
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.close();
                } else
                    dismiss();
            }
        });
        //去付款
        findViewById(R.id.go_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.goPay(type);
                }
            }
        });
        //列表
        RecyclerView rvTypes = (RecyclerView) findViewById(R.id.pay_type_list);
        types = new ArrayList<>();
        initPayTypesData(types);
        pt = new PayTypeAdapter(R.layout.pay_type_item, types);
        pt.setCallback(new PayTypeAdapter.Callback() {
            @Override
            public void onClicked(String arg0) {
                type = arg0;
            }
        });
        rvTypes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTypes.setAdapter(pt);

        View parent = (View) contentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        contentView.measure(0, 0);
        behavior.setPeekHeight(contentView.getMeasuredHeight());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        parent.setLayoutParams(params);

    }

    @Override
    public void show() {
        super.show();
        type = PayUtils.WX_PAY;
        tvPrice.setText(price);
        pt.notifyDataSetChanged();
    }

    private void initPayTypesData(List<PayType> types) {
        if (types != null)
            types.clear();
        types.add(new PayType(true, R.mipmap.pay_weixin, "微信支付", "推荐已安装微信用户使用", PayUtils.WX_PAY, true));
        types.add(new PayType(false, R.mipmap.pay_ali, "支付宝支付", "推荐已安装支付宝用户使用", PayUtils.ALI_PAY, true));
        if (balance != null) {
            if (Float.parseFloat(balance) >= Float.parseFloat(price))
                types.add(new PayType(false, R.mipmap.pay_balance_enabled,
                        "余额支付(剩余:¥" + balance + ")", "账户余额支付", PayUtils.BALANCE, true));
            else
                types.add(new PayType(false, R.mipmap.pay_balance_disabled,
                        "余额支付(剩余:¥" + balance + ")", "账户余额支付", PayUtils.BALANCE, false));
        }
    }

    public interface Callback {
        void goPay(String type);

        void close();
    }
}
