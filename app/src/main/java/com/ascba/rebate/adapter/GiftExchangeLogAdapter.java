package com.ascba.rebate.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.ExchangeEntity;
import com.squareup.picasso.Picasso;

/**
 * Created by Jero on 2017/9/22 0022.
 */

public class GiftExchangeLogAdapter extends BaseQuickAdapter<ExchangeEntity, BaseViewHolder> {
    public GiftExchangeLogAdapter() {
        super(R.layout.exchange_log_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExchangeEntity item) {
//        Picasso.with(mContext).load(TextUtils.isEmpty(item.getGoods_img())?null:item.getGoods_img()).placeholder(R.mipmap.exchange_loading).into((ImageView) helper.getView(R.id.item_exchange_iv));
        Picasso.with(mContext).load(Uri.parse(item.getGoods_img()))
                .placeholder(R.mipmap.module_loading).into((ImageView) helper.getView(R.id.item_exchange_iv));
        helper.setText(R.id.item_exchange_title, item.getGoods_title());
        helper.setText(R.id.item_exchange_accumulate, item.getScore_price());
        helper.setText(R.id.item_exchange_num, "X" + item.getGoods_num());
        helper.setText(R.id.item_exchange_time_tv, "兑换日期：" + item.getCreate_time());
        helper.setText(R.id.item_exchange_content, item.getStatus_text());
        helper.setText(R.id.item_exchange_type_tv, item.getStatus_tip());
        if (item.getIs_virtual() == 0) {
            helper.getView(R.id.item_exchange_logistics).setVisibility(item.getStatus() == 1 ? View.VISIBLE : View.INVISIBLE);
            helper.addOnClickListener(R.id.item_exchange_logistics);
        } else {
            helper.getView(R.id.item_exchange_logistics).setVisibility(View.GONE);
        }
    }
}
