package com.ascba.rebate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.BankCard;

import java.util.List;

/**
 * Created by 李平 on 2017/9/13 15:02
 * Describe: 银行卡列表
 */

public class BankCardAdapter extends BaseQuickAdapter<BankCard.BankListBean, BaseViewHolder> {
    public BankCardAdapter(@LayoutRes int layoutResId, @Nullable List<BankCard.BankListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BankCard.BankListBean item) {
        helper.setText(R.id.tv_name, item.getBank());
        helper.setText(R.id.tv_type, item.getNature());
        helper.setText(R.id.tv_number, "**** **** **** " + item.getBank_card());
        helper.addOnClickListener(R.id.tv_delete);
    }

    private String getTail4(String num) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        if (num != null) {
            int length = num.length();
            for (int i = 0; i < length - 4; i++) {
                if ((i + 1) % 4 == 0) {
                    sb.append("* ");
                } else {
                    sb.append("*");
                }

            }
            sb.append(" ");
            sb.append(num.substring(length - 4, length));
        }
        return sb.toString();
    }
}
