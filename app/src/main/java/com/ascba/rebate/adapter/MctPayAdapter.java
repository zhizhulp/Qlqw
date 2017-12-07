package com.ascba.rebate.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.MctBasePay;
import com.ascba.rebate.bean.MctPayClass;
import com.ascba.rebate.bean.MctPayDesc;
import com.ascba.rebate.bean.MctPayTitle;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MctPayAdapter extends BaseMultiItemQuickAdapter<MctBasePay, BaseViewHolder> {
    private int index=2;//默认当前选择的位置
    public MctPayAdapter(List<MctBasePay> data) {
        super(data);
        addItemType(MctBasePay.ITEM_TYPE_TITLE, R.layout.mct_title);
        addItemType(MctBasePay.ITEM_TYPE_CLASS, R.layout.mct_class);
        addItemType(MctBasePay.ITEM_TYPE_DESC, R.layout.mct_desc);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MctBasePay item) {
        switch (item.getItemType()) {
            case 0:
                helper.setText(R.id.tv_title, ((MctPayTitle) item).getTitle());
                break;
            case 1:
                MctPayClass payClass = (MctPayClass) item;
                final int position = helper.getAdapterPosition();
                helper.setText(R.id.tv_title, payClass.getTitle());
                helper.setText(R.id.tv_content, payClass.getContent());
                helper.setText(R.id.tv_now_price, payClass.getAfter());
                helper.setText(R.id.tv_before_price, payClass.getBefore());
                helper.getView(R.id.tv_title).setEnabled(position!=index);
                helper.getView(R.id.tv_content).setEnabled(position!=index);
                helper.getView(R.id.tv_now_price).setEnabled(position!=index);
                helper.getView(R.id.tv_before_price).setEnabled(position!=index);
                ((TextView) helper.getView(R.id.tv_before_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                helper.getView(R.id.lat_class).setEnabled(position!=index);
                helper.setOnClickListener(R.id.lat_class, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyItemChanged(index);
                        index = helper.getAdapterPosition();
                        notifyItemChanged(position);
                    }
                });
                break;
            case 2:
                MctPayDesc payDesc = (MctPayDesc) item;
                Picasso.with(mContext).load(payDesc.getDescImg()).placeholder(R.mipmap.module_loading).
                        into((ImageView) helper.getView(R.id.img));
                helper.setText(R.id.tv_title,payDesc.getTitle());
                helper.setText(R.id.tv_content,payDesc.getContent());
                break;
        }
    }

    public int getIndex() {
        return index;
    }
}
