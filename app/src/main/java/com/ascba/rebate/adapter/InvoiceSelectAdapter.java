package com.ascba.rebate.adapter;

import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.InvoiceSelect;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class InvoiceSelectAdapter extends BaseMultiItemQuickAdapter<InvoiceSelect, BaseViewHolder> {
    public static final int TYPE_HEAD = 1;
    public static final int TYPE_ITEM = 0;

    public String lastMonth, lastYear;
    public int paged = 1;

    public InvoiceSelectAdapter(List<InvoiceSelect> data) {
        super(data);
        addItemType(TYPE_HEAD, R.layout.item_invoice_head);
        addItemType(TYPE_ITEM, R.layout.item_invoice);
    }

    @Override
    protected void convert(final BaseViewHolder helper, InvoiceSelect item) {
        switch (item.getItemType()) {
            case TYPE_HEAD:
                helper.setText(R.id.tv_month, item.getMonth());
                break;
            case TYPE_ITEM:
                helper.setText(R.id.tv_title, item.getRemark());
                helper.setText(R.id.tv_time, item.getLog_time());
                helper.setText(R.id.tv_num, item.getMoney_text());
                helper.setChecked(R.id.cb_select, item.isSelect());
                helper.getView(R.id.cb_select).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helper.getConvertView().callOnClick();
                    }
                });
                break;
        }
    }
}
