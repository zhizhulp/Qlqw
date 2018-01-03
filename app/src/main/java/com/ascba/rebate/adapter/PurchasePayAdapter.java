package com.ascba.rebate.adapter;

import android.graphics.Paint;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.PurchaseEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


public class PurchasePayAdapter extends BaseQuickAdapter<PurchaseEntity.MoneyConfigBean, BaseViewHolder> {
    private int index = 0;//默认当前选择的位置

    public PurchasePayAdapter() {
        super(R.layout.item_purchase);
    }

    @Override
    protected void convert(BaseViewHolder helper, PurchaseEntity.MoneyConfigBean item) {
        helper.setText(R.id.tv_title, item.getCz_desc());
        helper.setText(R.id.tv_content, item.getCz_rate_money() + "分");
        if (item.getOnly_price() == 1) {
            helper.setText(R.id.tv_price_type, " ¥ ");
            helper.setText(R.id.tv_now_price, item.getCz_money());
            helper.setText(R.id.tv_before_price, "");
        } else if (item.getOnly_price() == 0) {
            helper.setText(R.id.tv_price_type, "折后价 ¥ ");
            helper.setText(R.id.tv_now_price, item.getCz_money());
            helper.setText(R.id.tv_before_price, " ¥ " + item.getCz_money());
            ((TextView) helper.getView(R.id.tv_before_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        boolean nuSelect = helper.getAdapterPosition() != index;
        helper.getView(R.id.tv_title).setEnabled(nuSelect);
        helper.getView(R.id.tv_content).setEnabled(nuSelect);
        helper.getView(R.id.tv_price_type).setEnabled(nuSelect);
        helper.getView(R.id.tv_now_price).setEnabled(nuSelect);
        helper.getView(R.id.tv_before_price).setEnabled(nuSelect);
        helper.getView(R.id.lat_item).setEnabled(nuSelect);
    }

    public PurchaseEntity.MoneyConfigBean getSelectItem() {
        return getItem(index);
    }

    public int getSelect() {
        return index;
    }

    public void setSelect(int select) {
        notifyItemChanged(index);
        this.index = select;
        notifyItemChanged(index);
    }
}
