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
    private float downx;
    private float downy;

    public MineViewPager(Context context) {
        super(context);
    }

    public MineViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent: "+ev.getAction());
        boolean b = super.dispatchTouchEvent(ev);
        Log.d(TAG, "dispatchTouchEvent: "+b);
        return b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            downx = ev.getX();
            downy = ev.getY();
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (downx - ev.getX() > 15 && downx - ev.getX() > Math.abs(downy - ev.getY())) {
                Log.d(TAG, "onInterceptTouchEvent: "+ev.getAction()+","+true);
                return true;
            }
        }
        boolean b = super.onInterceptTouchEvent(ev);
        Log.d(TAG, "onInterceptTouchEvent: "+ev.getAction()+","+b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean b = super.onTouchEvent(ev);
        Log.d(TAG, "onTouchEvent: "+ev.getAction()+","+b);
        return b;
    }

}
