package com.ascba.rebate.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.SellerEntity;
import com.squareup.picasso.Picasso;


public class SellerRecommendedAdapter extends BaseQuickAdapter<SellerEntity.ServerBean, BaseViewHolder> {

    public SellerRecommendedAdapter() {
        super(R.layout.item_seller_recommended);
    }

    @Override
    protected void convert(BaseViewHolder helper, SellerEntity.ServerBean item) {
        helper.setText(R.id.item_seller_content, item.getServer_desc());
        helper.setText(R.id.item_seller_title, item.getServer_title());
        Picasso.with(mContext).load(item.getServer_img())
                .into((ImageView) helper.getView(R.id.item_seller_iv));

    }
}
