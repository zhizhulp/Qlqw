package com.ascba.rebate.adapter;

import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.ShopEnter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;


public class ShopEnterTopAdapter extends BaseQuickAdapter<ShopEnter.PatternBean, BaseViewHolder> {

    public ShopEnterTopAdapter() {
        super(R.layout.item_shop_enter_btn);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopEnter.PatternBean item) {
        helper.setText(R.id.item_title, item.getPattern_title());
        Picasso.with(mContext).load(item.getPattern_img()).placeholder(R.mipmap.head_loading)
                .into((ImageView) helper.getView(R.id.item_icon));
    }
}
