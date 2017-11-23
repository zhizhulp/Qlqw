package com.ascba.rebate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.Bill;
import com.ascba.rebate.view.picasso.CropCircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李平 on 2017/9/19 10:22
 * Describe:筛选账单
 */

public class FilterBillAdapter extends BaseQuickAdapter<Bill,BaseViewHolder> {
    public FilterBillAdapter(@LayoutRes int layoutResId, @Nullable List<Bill> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bill item) {
        Picasso.with(mContext).load(item.getAvatar()).placeholder(R.mipmap.head_loading)
                .transform(new CropCircleTransformation())
                .into((ImageView) helper.getView(R.id.im_icon));
        helper.setText(R.id.tv_title,item.getRemark());
        helper.setText(R.id.tv_time,item.getLog_time());
        helper.setText(R.id.tv_money,item.getMoney());
        helper.setText(R.id.tv_status,item.getStatus_text());
    }
}
