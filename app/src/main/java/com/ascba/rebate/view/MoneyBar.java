package com.ascba.rebate.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;


/**
 * app自定义toolBar
 */

public class MoneyBar extends LinearLayout implements View.OnClickListener {
    private TextView mTextView;
    private String title;
    private CallBack callBack;
    private TextView tailText;
    private View bgView;

    public MoneyBar(Context context) {
        super(context);
    }

    public MoneyBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MoneyBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    //初始化
    private void initView(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.money_bar_layout, this, true);
        mTextView = (TextView) findViewById(R.id.money_bar_title);
        View backView = findViewById(R.id.money_bar_back_lat);
        bgView = findViewById(R.id.money_bar_parent);
        View line = findViewById(R.id.money_bar_line);
        ImageView backIm = (ImageView) findViewById(R.id.im_back);
        TextView tvBack = (TextView) findViewById(R.id.tv_back);
        tailText = (TextView) findViewById(R.id.tail_text);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MoneyBar);
        int bgColor = ta.getColor(R.styleable.MoneyBar_barBg, getResources().getColor(R.color.blue_btn));
        title = ta.getString(R.styleable.MoneyBar_textTitle);
        String tailStr = ta.getString(R.styleable.MoneyBar_textTail);
        int titleTextColor = ta.getColor(R.styleable.MoneyBar_titleColor, getResources().getColor(R.color.white));
        boolean needBack = ta.getBoolean(R.styleable.MoneyBar_needBack, true);
        boolean needLine = ta.getBoolean(R.styleable.MoneyBar_needLine, false);
        int backImgId = ta.getResourceId(R.styleable.MoneyBar_backImg, R.mipmap.back_white);
        int backTextColor = ta.getColor(R.styleable.MoneyBar_backTextColor, context.getResources().getColor(R.color.white));
        backIm.setBackgroundResource(backImgId);
        tvBack.setTextColor(backTextColor);
        //设置尾部文字
        setTextTail(tailStr);
        tailText.setOnClickListener(this);
        //设置title字体颜色
        mTextView.setTextColor(titleTextColor);
        //设置背景
        bgView.setBackgroundColor(bgColor);
        //设置是否需要返回图标
        if (!needBack) {
            backView.setVisibility(GONE);
        } else {
            backView.setOnClickListener(this);
        }
        //设置标题
        mTextView.setText(title);
        //设置是否有底部的横线
        if (needLine) {
            line.setVisibility(VISIBLE);
        } else {
            line.setVisibility(GONE);
        }
        ta.recycle();
    }

    //返回导航的功能
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.money_bar_back_lat:
                if (callBack != null) {
                    callBack.clickBack(v);
                } else {
                    ((Activity) getContext()).finish();
                }
                break;
            case R.id.tail_text:
                if (callBack != null) {
                    callBack.clickTail();
                }
                break;
        }
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void clickBack(View back);

        void clickTail();
    }

    public class CallbackImp implements CallBack {

        @Override
        public void clickBack(View back) {
            ((Activity) getContext()).finish();
        }

        @Override
        public void clickTail() {
        }
    }

    public void setTextTitle(String title) {
        this.title = title;
        mTextView.setText(title);
    }

    public void setTextTail(String tailStr) {
        if (tailStr != null && !tailStr.isEmpty()) {
            setTailShow(true);
            tailText.setText(tailStr);
        } else {
            setTailShow(false);
        }
    }

    public void setTailShow(boolean show) {
        tailText.setVisibility(show ? VISIBLE : GONE);
    }

    public void setBgColor(@ColorInt int color) {
        bgView.setBackgroundColor(color);
    }
}
