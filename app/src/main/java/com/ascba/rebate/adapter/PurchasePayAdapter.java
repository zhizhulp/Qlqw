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
        helper.setText(R.id.tv_content, item.getCz_rate_money());
        if (item.getOnly_price() == 1) {
            helper.setText(R.id.tv_price_type, " ¥ ");
            helper.setText(R.id.tv_now_price, item.getCz_money());
            helper.setText(R.id.tv_before_price, "");
            helper.setText(R.id.tv_dct, "");
            helper.setVisible(R.id.im_dct, false);
        } else if (item.getOnly_price() == 0) {
            helper.setText(R.id.tv_price_type, item.getCz_tip() + " ¥ ");
            helper.setText(R.id.tv_now_price, item.getCz_money());
            helper.setText(R.id.tv_before_price, " ¥ " + item.getCz_original_money());
            ((TextView) helper.getView(R.id.tv_before_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            helper.setText(R.id.tv_dct, "（" + item.getCz_corner() + "）");
            helper.setVisible(R.id.im_dct, true);
        }
        helper.getView(R.id.lat_item).setEnabled(helper.getAdapterPosition() != index);
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
