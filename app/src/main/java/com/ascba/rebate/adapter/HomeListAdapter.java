package com.ascba.rebate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.HomeBean;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HomeListAdapter extends BaseQuickAdapter<HomeBean.BillBean, BaseViewHolder> {
    public HomeListAdapter(@LayoutRes int layoutResId, @Nullable List<HomeBean.BillBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeBean.BillBean item) {
        Picasso.with(mContext).load(item.getIcon()).placeholder(R.mipmap.gift_goods_loading).into((ImageView) helper.getView(R.id.home_icon));
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_time, item.getTime());
        helper.setText(R.id.tv_money, item.getMoney());
        helper.setText(R.id.tv_success, item.getType());
        helper.setText(R.id.tv_det, item.getRemark());
        helper.addOnClickListener(R.id.tv_more);
        helper.setText(R.id.tv_more, item.getBillMore());
    }
}
