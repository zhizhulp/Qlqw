package com.ascba.rebate.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.MctBasePay;
import com.ascba.rebate.bean.MctPayAddress;
import com.ascba.rebate.bean.MctPayClass;
import com.ascba.rebate.bean.MctPayDesc;
import com.ascba.rebate.bean.MctPayTitle;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MctPayAdapter extends BaseMultiItemQuickAdapter<MctBasePay, BaseViewHolder> {
    private int index = 2;//默认当前选择的位置

    public MctPayAdapter(List<MctBasePay> data) {
        super(data);
        addItemType(MctBasePay.ITEM_TYPE_TITLE, R.layout.mct_title);
        addItemType(MctBasePay.ITEM_TYPE_CLASS, R.layout.mct_class);
        addItemType(MctBasePay.ITEM_TYPE_DESC, R.layout.mct_desc);
        addItemType(MctBasePay.ITEM_TYPE_ADDRESS, R.layout.item_agent_pay_address);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MctBasePay item) {
        switch (item.getItemType()) {
            case 0:
                MctPayTitle payTitle = (MctPayTitle) item;
                helper.setVisible(R.id.v_line, payTitle.isShowLine());
                helper.setVisible(R.id.space_top, payTitle.isShowSpace());
                helper.setText(R.id.tv_title, payTitle.getTitle());
                break;
            case 1:
                MctPayClass payClass = (MctPayClass) item;
                final int position = helper.getAdapterPosition();
                helper.setText(R.id.tv_title, payClass.getTitle());
                helper.setText(R.id.tv_content, payClass.getContent());
                if (payClass.getOnly_price() == 1) {
                    helper.setText(R.id.tv_price_type, " ¥ ");
                    helper.setText(R.id.tv_now_price, payClass.getBefore());
                    helper.setText(R.id.tv_before_price, "");
                } else if (payClass.getOnly_price() == 0) {
                    helper.setText(R.id.tv_price_type, "特惠价 ¥ ");
                    helper.setText(R.id.tv_now_price, payClass.getAfter());
                    helper.setText(R.id.tv_before_price, " ¥ " + payClass.getBefore());
                    ((TextView) helper.getView(R.id.tv_before_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                boolean nuSelect = position != index;
                helper.getView(R.id.tv_title).setEnabled(nuSelect);
                helper.getView(R.id.tv_content).setEnabled(nuSelect);
                helper.getView(R.id.tv_price_type).setEnabled(nuSelect);
                helper.getView(R.id.tv_now_price).setEnabled(nuSelect);
                helper.getView(R.id.tv_before_price).setEnabled(nuSelect);
                helper.getView(R.id.lat_class).setEnabled(nuSelect);

                helper.setOnClickListener(R.id.lat_class, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyItemChanged(index);
                        index = position;
                        notifyItemChanged(index);
                    }
                });
                break;
            case 2:
                MctPayDesc payDesc = (MctPayDesc) item;
                Picasso.with(mContext).load(payDesc.getDescImg()).placeholder(R.mipmap.module_loading).
                        into((ImageView) helper.getView(R.id.img));
                helper.setText(R.id.tv_title, payDesc.getTitle());
                helper.setText(R.id.tv_content, payDesc.getContent());
                helper.setVisible(R.id.v_line, !payDesc.isLast());
                break;
            case 3:
                MctPayAddress payAddress = (MctPayAddress) item;
                if (payAddress.getIsBuyAgency() == 1) {
                    helper.getView(R.id.tv_address_select).setVisibility(View.INVISIBLE);
                    helper.setVisible(R.id.im_more, false);
                    helper.setText(R.id.tv_address, payAddress.getAddress());
                } else if (payAddress.getIsBuyAgency() == 0) {
                    helper.getView(R.id.tv_address_select).setVisibility(View.VISIBLE);
                    helper.setVisible(R.id.im_more, true);
                    helper.addOnClickListener(R.id.lat_address);
                } else if (payAddress.getIsBuyAgency() == 2) {
                    helper.getView(R.id.tv_address_select).setVisibility(View.VISIBLE);
                    helper.setVisible(R.id.im_more, true);
                    helper.setText(R.id.tv_address, "选择地区");
                    helper.addOnClickListener(R.id.lat_address);
                    helper.setText(R.id.tv_address_select, payAddress.getAddress());
                }
                break;
        }
    }

    public MctPayClass getSelect() {
        return (MctPayClass) getItem(index - 1);
    }
}
