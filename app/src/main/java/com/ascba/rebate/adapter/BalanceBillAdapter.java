package com.ascba.rebate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.BalanceBill;
import java.util.List;

/**
 * Created by 李平 on 2017/9/11 18:04
 * Describe:
 */

public class BalanceBillAdapter extends BaseQuickAdapter<BalanceBill,BaseViewHolder> {
    public BalanceBillAdapter(@LayoutRes int layoutResId, @Nullable List<BalanceBill> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BalanceBill item) {
        helper.setText(R.id.tv_type_string,item.getDesc());
        helper.setText(R.id.tv_time,item.getTime());
        helper.setText(R.id.tv_balance,item.getBalance());
        helper.setText(R.id.tv_flow,item.getFlow());
    }
}
