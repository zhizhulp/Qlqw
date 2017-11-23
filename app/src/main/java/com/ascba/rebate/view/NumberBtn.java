package com.ascba.rebate.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;

/**
 * Created by 李平 on 2017/9/20 16:35
 * Describe: 带加减号的按钮
 */

public class NumberBtn extends LinearLayout implements View.OnClickListener {
    private TextView tvNum;
    private int stock = Integer.MAX_VALUE;//库存
    private Callback callback;
    public View subBtn;//减
    public View addBtn;//加

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public NumberBtn(Context context) {
        super(context);
    }

    public NumberBtn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.number_btn, this, true);
        subBtn = findViewById(R.id.im_sub);
        subBtn.setOnClickListener(this);
        addBtn = findViewById(R.id.im_add);
        addBtn.setOnClickListener(this);
        tvNum = ((TextView) findViewById(R.id.tv_num));
    }

    @Override
    public void onClick(View v) {
        String s = tvNum.getText().toString();
        int i = Integer.parseInt(s);
        switch (v.getId()) {
            case R.id.im_sub:
                if (i <= 1) {
                    return;
                }
                tvNum.setText(String.valueOf(i - 1));
                if (callback != null) {
                    callback.clickSub(i - 1);
                }
                break;
            case R.id.im_add:
                if (i + 1 > stock) {
                    Toast.makeText(getContext(), "数量超出范围", Toast.LENGTH_SHORT).show();
                    if (callback != null) {
                        callback.stockWarn();
                    }
                    return;
                }
                tvNum.setText(String.valueOf(i + 1));
                if (callback != null) {
                    callback.clickAdd(i + 1);
                }
                break;
        }
    }

    public void setNum(String setNum) {
        tvNum.setText(setNum);
    }

    public int getNum() {
        return Integer.parseInt(tvNum.getText().toString());
    }

    public interface Callback {
        void clickSub(int sub);

        void clickAdd(int add);

        void stockWarn();
    }
}
