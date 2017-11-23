package com.ascba.rebate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.ascba.rebate.R;


public class RectangleBoxEditText extends android.support.v7.widget.AppCompatEditText {
    //画笔
    private Paint yellowPaint;
    private Paint boxBackgroundPaint;
    private Paint numberPaint;

    private int mWidth;
    private int mHeight;
    //需要的自定义颜色
    private int boxStokeColor;          //每个box的边框颜色，这里是黄色
    private int boxBackgroundColor;     //每个box内部的颜色，这里是灰色
    private int numberTextColor;        //数字的颜色

    private int boxNumber;//box的数量，也就是验证码的长度
    private int boxPadding;

    private int averageBoxWidth; //每个Box的宽
    private String smsCode; //验证码
    private Rect smsRect = new Rect();
    private Paint.FontMetricsInt fm;
    private OnInputFinishedListener onInputFinishedListener;

    public RectangleBoxEditText(Context context) {
        this(context, null);
    }

    public RectangleBoxEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectangleBoxEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RectangleBoxEditText, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.RectangleBoxEditText_boxBackgroundColor:
                    boxBackgroundColor = a.getColor(R.styleable.RectangleBoxEditText_boxBackgroundColor, Color.RED);//默认是白色
                    break;
                case R.styleable.RectangleBoxEditText_boxStokeColor:
                    boxStokeColor = a.getColor(attr, getResources().getColor(R.color.grey_line));//默认是灰色
                    break;
                case R.styleable.RectangleBoxEditText_numberTextColor:
                    numberTextColor = a.getColor(attr, Color.BLACK);//默认黑色
                    break;
                case R.styleable.RectangleBoxEditText_boxCount:
                    boxNumber = a.getInt(attr, 6);//默认为6个Box
                    break;
                case R.styleable.RectangleBoxEditText_boxPadding:
                    boxPadding = a.getDimensionPixelSize(attr, 0);//默认为没间距
                    break;
            }
        }
        a.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        averageBoxWidth = (mWidth - (boxNumber - 1) * boxPadding) / boxNumber;

    }

    private void init() {
        yellowPaint = new Paint();
        yellowPaint.setAntiAlias(true);
        yellowPaint.setColor(boxStokeColor);
        yellowPaint.setStyle(Paint.Style.STROKE);
        yellowPaint.setStrokeWidth(4);
        numberPaint = new Paint();
        numberPaint.setFakeBoldText(true);
        numberPaint.setTextSize(20);
        numberPaint.setAntiAlias(true);
        numberPaint.setColor(numberTextColor);
        numberPaint.setStyle(Paint.Style.FILL);
        numberPaint.setTextSize(getTextSize());
        numberPaint.setTextAlign(Paint.Align.CENTER);
        boxBackgroundPaint = new Paint();
        boxBackgroundPaint.setAntiAlias(true);
        boxBackgroundPaint.setColor(boxBackgroundColor);
        boxBackgroundPaint.setStyle(Paint.Style.FILL);
        fm = numberPaint.getFontMetricsInt();

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        for (int i = 0; i < boxNumber; i++) {
            int left = i * averageBoxWidth + i * boxPadding;
            int top = 0;
            int right = (i + 1) * averageBoxWidth + i * boxPadding;
            int bottom = mHeight;
            canvas.drawLine(left+dp2px(getContext(), 2),bottom-dp2px(getContext(), 2),right-dp2px(getContext(), 2),bottom-dp2px(getContext(), 2),boxBackgroundPaint);
        }
        if (!TextUtils.isEmpty(smsCode)) {
            for (int i = 0; i < smsCode.length(); i++) {
                numberPaint.getTextBounds(smsCode, i, i + 1, smsRect);
                canvas.drawText(smsCode, i, i + 1, (averageBoxWidth / 2) + averageBoxWidth * i + i * boxPadding, mHeight / 2 - fm.descent + (fm.bottom - fm.top) / 2, numberPaint);
                int left = i * averageBoxWidth + i * boxPadding;
                int right = (i + 1) * averageBoxWidth + i * boxPadding;
                int bottom = mHeight;
                canvas.drawLine(left+dp2px(getContext(), 2),bottom-dp2px(getContext(), 2),right-dp2px(getContext(), 2),bottom-dp2px(getContext(), 2),yellowPaint);
            }

        }

    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() != 0) {
            smsCode = text.toString();
        } else {
            smsCode = "";
        }
        if (text.length() == boxNumber) {   //输入完成
            if(onInputFinishedListener != null)
                onInputFinishedListener.onInputFinished(smsCode);
        }
        postInvalidate();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void add(String addStr){
        smsCode += addStr;
        setText(smsCode);
    }

    public void delete(){
        if(smsCode.length() > 0){
            smsCode = smsCode.substring(0,smsCode.length()-1);
            setText(smsCode);
        }
    }

    public interface OnInputFinishedListener {
        void onInputFinished(String password);
    }

    public void setOnInputFinishedListener(OnInputFinishedListener onInputFinishedListener) {
        this.onInputFinishedListener = onInputFinishedListener;
    }
}
