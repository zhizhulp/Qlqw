package com.ascba.rebate.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/1/27.
 */
public class MineConstraintLayout extends ConstraintLayout {
    private String TAG = "ConstraintLayout";

    public MineConstraintLayout(Context context) {
        super(context);
    }

    public MineConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MineConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent: " + ev.getAction());
        boolean b = super.dispatchTouchEvent(ev);
        Log.d(TAG, "dispatchTouchEvent: " + b);
        return b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean b = super.onInterceptTouchEvent(ev);
        Log.d(TAG, "onInterceptTouchEvent: " + ev.getAction() + "," + b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean b = super.onTouchEvent(ev);
        Log.d(TAG, "onTouchEvent: " + ev.getAction() + "," + b);
        return b;
    }

}
