package com.ascba.rebate.adapter;

import android.view.View;

import com.ascba.rebate.BuildConfig;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.PayType;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * 支付类型适配器
 */

public class DebugAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Callback callback;
    private String select = UrlUtils.baseWebsite;

    public interface Callback {
        void onClicked(String payType);
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public DebugAdapter(List<String> data) {
        super(R.layout.debug_item, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        helper.setChecked(R.id.debug_type_cb, select.equals(item));
        helper.setOnClickListener(R.id.top, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheck(item);
            }
        });
        helper.setOnClickListener(R.id.debug_type_cb, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheck(item);
            }
        });
        helper.setText(R.id.debug_type_title, item);
    }

    private void setCheck(String item) {
        if (select.equals(item))
            return;
        select = item;
        notifyDataSetChanged();
        if (callback != null) {
            callback.onClicked(item);
        }
    }
}
