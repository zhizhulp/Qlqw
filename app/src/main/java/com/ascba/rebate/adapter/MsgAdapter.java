package com.ascba.rebate.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.MsgEntity;
import com.squareup.picasso.Picasso;


public class MsgAdapter extends BaseQuickAdapter<MsgEntity, BaseViewHolder> {

    public MsgAdapter() {
        super(R.layout.msg_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MsgEntity item) {
        helper.setText(R.id.tv_type_string, item.getTitle());
        helper.setText(R.id.tv_time, item.getCreate_time());
        helper.setText(R.id.tv_msg, item.getMessageTitle());
        helper.getView(R.id.iv_read).setVisibility(item.getIs_read() == 1 ? View.GONE : View.VISIBLE);
        switch (item.getClass_id()) {
            case 26://系统消息
                helper.setImageResource(R.id.iv_type, R.mipmap.msg_sys);
                break;
            case 27://优惠促销
                helper.setImageResource(R.id.iv_type, R.mipmap.msg_promotion);
                break;
            case 35://消息中心
                helper.setImageResource(R.id.iv_type, R.mipmap.msg_center);
                break;
            default:
                Picasso.with(mContext).load(item.getPic()).into((ImageView) helper.getView(R.id.iv_type));
                break;
        }
    }
}
