package com.ascba.rebate.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.BenefitMain;
import com.ascba.rebate.view.picasso.CropCircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李平 on 2017/10/30 11:36
 * Describe:寄卖收益
 */

public class BenefitAdapter extends BaseQuickAdapter<BenefitMain.Benefit, BaseViewHolder> {
    private int[] rankingIcons = new int[]{R.mipmap.num_01, R.mipmap.num_02, R.mipmap.num_03};

    public BenefitAdapter(int layoutResId, @Nullable List<BenefitMain.Benefit> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BenefitMain.Benefit item) {
        int position = helper.getAdapterPosition();
        if (position <= 3) {
            helper.setVisible(R.id.tv_ranking, false);
            helper.setVisible(R.id.im_ranking, true);
            Picasso.with(mContext).load(rankingIcons[position-1]).placeholder(R.mipmap.head_loading)
                    .into((ImageView) helper.getView(R.id.im_ranking));
        } else {
            helper.setVisible(R.id.tv_ranking, true);
            helper.setVisible(R.id.im_ranking, false);
            helper.setText(R.id.tv_ranking, position + "");
        }

        String avatar = item.getAvatar();
        if (!TextUtils.isEmpty(avatar)) {
            Picasso.with(mContext).load(avatar).transform(new CropCircleTransformation())
                    .placeholder(R.mipmap.head_loading).into((ImageView) helper.getView(R.id.im_head));
        }
        helper.setText(R.id.tv_name, item.getRealname());
        helper.setText(R.id.tv_money, item.getRed_score());
    }
}
