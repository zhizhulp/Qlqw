package com.ascba.rebate.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.Bill;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李平 on 2017/9/18 10:38
 * Describe:账单
 */

public class BillAdapter extends BaseMultiItemQuickAdapter<Bill,BaseViewHolder> {
    public static final int TYPE_HEAD = 1;
    public static final int TYPE_ITEM = 0;
    public BillAdapter(List<Bill> data) {
        super(data);
        addItemType(TYPE_HEAD, R.layout.bill_head);
        addItemType(TYPE_ITEM, R.layout.bill_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bill item) {
        switch (item.getItemType()){
            case TYPE_HEAD:
                helper.setText(R.id.tv_month,item.getMonth());
                helper.addOnClickListener(R.id.im_calendar);
                helper.setVisible(R.id.im_calendar,item.isHeadIconVisible());
                break;
            case TYPE_ITEM:
                Picasso.with(mContext).load(item.getAvatar()).placeholder(R.mipmap.head_loading).
                        into((ImageView) helper.getView(R.id.im_icon));
                helper.setText(R.id.tv_title,item.getRemark());
                helper.setText(R.id.tv_time,item.getLog_time());
                helper.setText(R.id.tv_money,item.getMoney());
                helper.setText(R.id.tv_status,item.getStatus_text());
                break;
        }
    }
}
