package com.ascba.rebate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.BankCard;

import java.util.List;

/**
 * Created by 李平 on 2017/9/14 11:09
 * Describe:选择银行卡
 */

public class SelBCardAdapter extends BaseQuickAdapter<BankCard.BankListBean, BaseViewHolder> {
    private List<BankCard.BankListBean> data;
    private Callback callback;

    public SelBCardAdapter(@LayoutRes int layoutResId, @Nullable List<BankCard.BankListBean> data) {
        super(layoutResId, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, final BankCard.BankListBean item) {
        helper.setText(R.id.radioButton, item.getBank());
        helper.setText(R.id.tv_type, item.getNature());
        helper.setChecked(R.id.radioButton, item.getIs_default() == 1);
        helper.setText(R.id.tv_num, "**** " + item.getBank_card());

        helper.setOnClickListener(R.id.lat_parent, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setIs_default(1);
                for (int i = 0; i < data.size(); i++) {
                    BankCard.BankListBean bankCard = data.get(i);
                    if (bankCard.getIs_default() == 1 && item != bankCard) {
                        bankCard.setIs_default(0);
                    }
                }

                notifyDataSetChanged();
                if (callback != null) {
                    callback.onClicked(item);
                }
            }
        });
        helper.setOnClickListener(R.id.radioButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setIs_default(1);
                for (int i = 0; i < data.size(); i++) {
                    BankCard.BankListBean bankCard = data.get(i);
                    if (bankCard.getIs_default() == 1 && item != bankCard) {
                        bankCard.setIs_default(0);
                    }
                }

                notifyDataSetChanged();
                if (callback != null) {
                    callback.onClicked(item);
                }
            }
        });
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onClicked(BankCard.BankListBean bankCard);
    }
}
