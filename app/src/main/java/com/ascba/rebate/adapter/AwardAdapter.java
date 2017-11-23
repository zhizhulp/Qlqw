package com.ascba.rebate.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.InviteAll;
import com.ascba.rebate.view.picasso.CropCircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AwardAdapter extends BaseQuickAdapter<InviteAll.RecAward, BaseViewHolder> {
    public AwardAdapter(int layoutResId, @Nullable List<InviteAll.RecAward> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InviteAll.RecAward item) {
        Picasso.with(mContext).load(item.getAvatar())
                .placeholder(R.mipmap.head_loading)
                .transform(new CropCircleTransformation())
                .into((ImageView) helper.getView(R.id.im_head));
        helper.setText(R.id.tv_title,item.getNickname());
        helper.setText(R.id.tv_time,item.getCreate_time());
        helper.setText(R.id.tv_score,item.getWhite_score()+"");
    }
}
