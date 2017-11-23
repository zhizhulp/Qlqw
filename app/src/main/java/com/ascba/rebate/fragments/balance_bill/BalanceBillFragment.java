package com.ascba.rebate.fragments.balance_bill;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.BalanceBillAdapter;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.bean.BalanceBill;
import com.ascba.rebate.utils.NumberFormatUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2017/9/11 17:03
 * Describe: 余额明细基类
 */

public class BalanceBillFragment extends BaseDefaultNetFragment {
    private int type;
    private String [] typeDesc=new String[]{"充值","体现","消费","兑换"};

    public BalanceBillFragment() {
    }

    public static BalanceBillFragment getInstance(int type) {
        BalanceBillFragment fragment = new BalanceBillFragment();
        Bundle b = new Bundle();
        b.putInt("type", 0);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_balance_bill;
    }

    @Override
    protected void initViews() {
        super.initViews();
        getParams();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new BalanceBillAdapter(R.layout.balance_bill_item,getBillList(type)));
    }

    private void getParams() {
        Bundle bundle = getArguments();
        type = bundle.getInt("type");
    }

    private List<BalanceBill> getBillList(int type) {
        List<BalanceBill> list = new ArrayList<>();
        for (int i = 0; i < (int) (Math.random() * 30.0 + 10.0); i++) {
            list.add(new BalanceBill(type,typeDesc[(int)(Math.random()*4.0)],
                    new SimpleDateFormat("yy-MM-dd hh:mm").format(System.currentTimeMillis()),
                    "余额"+ NumberFormatUtils.getNewDouble(Math.random()*5000.0+100.0),
                    "+"+NumberFormatUtils.getNewDouble(Math.random()*5000.0+1.0)));
        }
        return list;
    }
}
