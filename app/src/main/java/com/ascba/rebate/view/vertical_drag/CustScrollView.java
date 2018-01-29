package com.ascba.rebate.view.vertical_drag;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 只为顶部ScrollView使用
 * 如果使用了其它的可拖拽的控件，请仿照该类实现isAtBottom方法
 */
public class CustScrollView extends ScrollView {
    private ScrollListener scrollListener;
    private String TAG="CustScrollView";

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public CustScrollView(Context arg0) {
        this(arg0, null);
    }

    public CustScrollView(Context arg0, AttributeSet arg1) {
        this(arg0, arg1, 0);
    }

    public CustScrollView(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollListener != null) {
            scrollListener.onScrollChange(l, t, oldl, oldt);
        }
    }

    public interface ScrollListener {
        void onScrollChange(int l, int t, int oldl, int oldt);
    }
}
