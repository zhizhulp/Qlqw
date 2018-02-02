package com.ascba.rebate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 */
public class STTabAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public STTabAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(android.R.id.text1,item);
    }
}
