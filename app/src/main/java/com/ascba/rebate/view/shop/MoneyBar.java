package com.ascba.rebate.view.shop;

import android.content.Context;
import android.util.AttributeSet;

import com.ascba.rebate.R;


/**
 * app自定义toolBar
 */

public class MoneyBar extends com.ascba.rebate.view.MoneyBar {

    public MoneyBar(Context context) {
        super(context);
    }

    public MoneyBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoneyBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected int initLayout() {
        return R.layout.shop_bar_layout;
    }

    protected int initBgColor() {
        return getResources().getColor(R.color.white);
    }

    protected int initTitleColor() {
        return getResources().getColor(R.color.grey_black_tv);
    }

    protected int initBackImg() {
        return R.mipmap.back_small;
    }

    protected int initBackColor() {
        return getResources().getColor(R.color.grey_black_tv);
    }
}
