package com.ascba.rebate.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/1/26.
 */
public class MineViewPager extends ViewPager {
    private String TAG = "ViewPager";

    public MineViewPager(Context context) {
        super(context);
    }

    public MineViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Log.d(TAG, "dispatchTouchEvent: "+ev.getAction());
        boolean b = super.dispatchTouchEvent(ev);
        //Log.d(TAG, "dispatchTouchEvent: "+b);
        return b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //Log.d(TAG, "dispatchTouchEvent: "+ev.getAction());
        boolean b = super.onInterceptTouchEvent(ev);
        //Log.d(TAG, "onInterceptTouchEvent: "+b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //Log.d(TAG, "dispatchTouchEvent: "+ev.getAction());
        boolean b = super.onTouchEvent(ev);
        //Log.d(TAG, "onTouchEvent: "+b);
        return b;
    }
}
