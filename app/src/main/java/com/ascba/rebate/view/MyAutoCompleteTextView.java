package com.ascba.rebate.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by 李平 on 2017/12/4 16:12
 * Describe: 用户没有输入时显示所有
 */

public class MyAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {
    public MyAutoCompleteTextView(Context context) {
        super(context);
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        performFiltering(getText(), KeyEvent.KEYCODE_UNKNOWN);
    }
}
