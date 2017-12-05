package com.ascba.rebate.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.ArroundEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;


public class ArroundAdapter extends BaseQuickAdapter<ArroundEntity, BaseViewHolder> {

    public ArroundAdapter() {
        super(R.layout.item_arround);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ArroundEntity item) {
        Picasso.with(mContext).load(Uri.parse(item.getSeller_cover_logo()))
                .placeholder(R.mipmap.module_loading).into((ImageView) helper.getView(R.id.item_iv));
        helper.setText(R.id.item_title, item.getSeller_name());
        helper.setText(R.id.item_taglib, item.getSeller_taglib());
        helper.setText(R.id.item_time_tv, "营业时间：" + item.getSeller_business_hours());
        helper.setText(R.id.item_distance_tv, item.getEarth_radius());
        helper.setText(R.id.item_context_tv, item.getSeller_description());
        helper.setText(R.id.item_address_tv, item.getSeller_address());
        if (item.isOpen()) {
            ((TextView) helper.getView(R.id.item_context_tv)).setMaxLines(Integer.MAX_VALUE);
            helper.setImageResource(R.id.item_open_iv, R.mipmap.arrows_up);
        } else {
            ((TextView) helper.getView(R.id.item_context_tv)).setMaxLines(2);
            helper.setImageResource(R.id.item_open_iv, R.mipmap.arrows_down);
        }
        helper.setOnClickListener(R.id.item_context_tv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setOpen(!item.isOpen());
                notifyDataSetChanged();
            }
        });
    }
}
