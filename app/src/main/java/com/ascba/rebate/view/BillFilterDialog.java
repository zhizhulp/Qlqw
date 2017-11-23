package com.ascba.rebate.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.BillFilter;

import java.util.List;

/**
 * Created by 李平 on 2017/9/19 14:05
 * Describe: 账单筛选
 */

public class BillFilterDialog extends BottomSheetDialog {
    private Callback callback;
    private List<BillFilter> data;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public BillFilterDialog(@NonNull Context context, List<BillFilter> data) {
        super(context);
        this.data = data;
        init();
    }

    private void init() {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.bill_filter_dlg);
        final RadioGroupEx radioGroupEx = (RadioGroupEx) findViewById(R.id.radioGroup);
        for (int i = 0; i < data.size(); i++) {
            BillFilter bf = data.get(i);
            final RadioButton rb = (RadioButton) LayoutInflater.from(getContext()).
                    inflate(R.layout.filter_dlg_radio_button, radioGroupEx, false);
            rb.setText(bf.getType_text());
            rb.setTag(bf);
            rb.setChecked(bf.isSelect());
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BillFilter rbTag = (BillFilter) rb.getTag();
                    int count = radioGroupEx.getChildCount();
                    for (int j = 0; j < count; j++) {
                        RadioButton childAt = (RadioButton) radioGroupEx.getChildAt(j);
                        if(childAt==v){
                            childAt.setChecked(true);
                        }else {
                            childAt.setChecked(false);
                        }
                    }
                    if(callback!=null){
                        callback.onItemClick(rbTag);
                    }

                }
            });
            radioGroupEx.addView(rb);
        }

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public interface Callback{
        void onItemClick(BillFilter bf);
    }
}
