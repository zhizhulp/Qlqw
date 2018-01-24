package com.ascba.rebate.adapter;

import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.MsgListEntity;
import com.ascba.rebate.utils.DrawableChangeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


public class MsgListAdapter extends BaseQuickAdapter<MsgListEntity, BaseViewHolder> {

    public MsgListAdapter() {
        super(R.layout.msg_list_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MsgListEntity item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_time, item.getCreate_time());
        helper.setText(R.id.tv_intro, item.getIntro());
        if (item.getIs_read() == 0) {
            helper.setTextColor(R.id.tv_title, mContext.getResources().getColor(R.color.grey_black_tv));
            helper.setTextColor(R.id.tv_intro, mContext.getResources().getColor(R.color.grey_black_tv));
            helper.setTextColor(R.id.tv_see, mContext.getResources().getColor(R.color.grey_black_tv));
            DrawableChangeUtils.setChangeDrawable((TextView) helper.getView(R.id.tv_see), DrawableChangeUtils.Drawable_RIGHT, R.mipmap.more);
        } else {
            helper.setTextColor(R.id.tv_title, mContext.getResources().getColor(R.color.grey_black_tv3));
            helper.setTextColor(R.id.tv_intro, mContext.getResources().getColor(R.color.grey_tv));
            helper.setTextColor(R.id.tv_see, mContext.getResources().getColor(R.color.grey_black_tv3));
            DrawableChangeUtils.setChangeDrawable((TextView) helper.getView(R.id.tv_see), DrawableChangeUtils.Drawable_RIGHT, R.mipmap.more_gray);
        }
    }
}
