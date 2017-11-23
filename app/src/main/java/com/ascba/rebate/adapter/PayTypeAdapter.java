package com.ascba.rebate.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.PayType;

import java.util.List;


/**
 * 支付类型适配器
 */

public class PayTypeAdapter extends BaseQuickAdapter<PayType, BaseViewHolder> {
    private Callback callback;

    public interface Callback {
        void onClicked(String payType);
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public PayTypeAdapter(int layoutResId, List<PayType> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final PayType item) {
        helper.setVisible(R.id.pay_type_content, item.getContent() != null);
        helper.getView(R.id.pay_type_cb).setEnabled(item.isEnable());
        helper.setChecked(R.id.pay_type_cb, item.isSelect());
        helper.getView(R.id.top).setEnabled(item.isEnable());
        helper.setOnClickListener(R.id.top, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheck(item);
            }
        });
        helper.setOnClickListener(R.id.pay_type_cb, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheck(item);
            }
        });
        helper.setImageResource(R.id.pay_icon, item.getIcon());
        helper.setText(R.id.pay_type_title, item.getTitle());
        helper.setText(R.id.pay_type_content, item.getContent());
        if (item.isEnable()) {
            helper.setTextColor(R.id.pay_type_title, mContext.getResources().getColor(R.color.grey_black_tv));
            helper.setTextColor(R.id.pay_type_content, mContext.getResources().getColor(R.color.grey_black_tv2));
        } else {
            helper.setTextColor(R.id.pay_type_title, mContext.getResources().getColor(R.color.grey_black_tv3));
            helper.setTextColor(R.id.pay_type_content, mContext.getResources().getColor(R.color.grey_tv));
        }
    }

    private void setCheck(PayType item) {
        if (item.isSelect())
            return;
        item.setSelect(true);
        for (int i = 0; i < mData.size(); i++) {
            PayType payType = mData.get(i);
            if (payType.isSelect() && item != payType) {
                payType.setSelect(false);
            }
        }
        notifyDataSetChanged();
        if (callback != null) {
            callback.onClicked(item.getType());
        }
    }
}
