package com.ascba.rebate.adapter;

import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.InvoiceHistoryEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Jero on 2017/9/22 0022.
 */

public class InvoiceHistoryAdapter extends BaseQuickAdapter<InvoiceHistoryEntity, BaseViewHolder> {
    public InvoiceHistoryAdapter() {
        super(R.layout.item_invoice_history);
    }

    @Override
    protected void convert(BaseViewHolder helper, InvoiceHistoryEntity item) {
        helper.setText(R.id.item_exchange_title, item.getMember_info());
        helper.setText(R.id.item_exchange_num, "开票金额：" + item.getTotal_fee());
        helper.setText(R.id.item_exchange_time_tv, "兑换日期：" + item.getCreate_time());
        helper.setText(R.id.item_exchange_content, "开票状态：" + item.getStatus());
        helper.setText(R.id.item_exchange_type_tv, "邮寄状态：" + item.getShipping());
        if (item.getWu_url() == 1) {
            helper.getView(R.id.item_exchange_logistics).setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.item_exchange_logistics);
        } else {
            helper.getView(R.id.item_exchange_logistics).setVisibility(View.GONE);
        }
    }
}
