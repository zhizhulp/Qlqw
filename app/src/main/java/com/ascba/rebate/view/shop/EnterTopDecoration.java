package com.ascba.rebate.view.shop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.utils.ScreenDpiUtils;

/**
 * Created by Jero on 2018/1/29 0029.
 */

public class EnterTopDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private int dividerHeight;
    private int move;
    private int padding;
    private int columnCount = 2;
    private Rect mBounds = new Rect();

    public EnterTopDecoration(Context context) {
        mContext = context;
        setDivider(context, 1);
    }

    public EnterTopDecoration(Context context, int dividerDP) {
        this(context);
        setDivider(context, dividerDP);
    }

    public EnterTopDecoration(Context context, int dividerDP, int lineCount) {
        this(context, dividerDP);
        setHorizontalCount(lineCount);
    }

    public EnterTopDecoration setDivider(Context context, int dp) {
        dividerHeight = (int) ScreenDpiUtils.dp2px(context, dp);
        move = dividerHeight / 2;
        padding = (int) ScreenDpiUtils.dp2px(context, 12);
        return this;
    }

    public EnterTopDecoration setHorizontalCount(int lineCount) {
        columnCount = lineCount;
        return this;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int top = dividerHeight;
        int left = 0;
        int right = 0;
        if (position < columnCount)
            top = 0;
        if (position % 2 == 1)
            left = move;
        else
            right = move;
        outRect.set(left, top, right, 0);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        draw(c, parent);
    }

    private void draw(Canvas canvas, RecyclerView parent) {
        canvas.save();
        Paint paint = new Paint();
        paint.setColor(mContext.getResources().getColor(R.color.grey_line));
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            int left = mBounds.left + padding;
            int right = mBounds.right - padding;
            int top = mBounds.top + padding;
            int bottom = mBounds.bottom - padding;
            if (i >= columnCount)
                top += dividerHeight;
            if (i % 2 == 0) {
                right -= move;
                //draw  VERTICAL
                canvas.drawRect(mBounds.right - move, top,
                        mBounds.right + move, bottom, paint);
            } else {
                left += move;
            }
            if (i <= childCount - columnCount) {
                //draw  HORIZONTAL
                canvas.drawRect(left, mBounds.bottom,
                        right, mBounds.bottom + dividerHeight, paint);
            }
        }
        canvas.restore();
    }
}
