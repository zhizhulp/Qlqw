package com.ascba.rebate.view.timeline;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.StepBean;

import java.util.List;

/**
 * 日期：16/6/22 15:47
 * <p/>
 * 描述：StepView
 */
public class HorizontalStepView extends LinearLayout implements HorizontalStepsViewIndicator.OnDrawIndicatorListener {
    private RelativeLayout mTextContainer;
    private HorizontalStepsViewIndicator mStepsViewIndicator;
    private List<StepBean> mStepBeanList;

    private int mTextSize = 13;//default textSize
    private int mComplectingPosition;

    public HorizontalStepView(Context context) {
        this(context, null);
    }

    public HorizontalStepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_horizontal_stepsview, this);
        mStepsViewIndicator = (HorizontalStepsViewIndicator) rootView.findViewById(R.id.steps_indicator);
        mStepsViewIndicator.setOnDrawListener(this);
        mTextContainer = (RelativeLayout) rootView.findViewById(R.id.rl_text_container);
    }

    /**
     * 设置显示的文字
     */
    public HorizontalStepView setStepViewTexts(List<StepBean> stepsBeanList) {
        mStepBeanList = stepsBeanList;
        mStepsViewIndicator.setStepNum(mStepBeanList);
        return this;
    }

    /**
     * 设置StepsViewIndicator未完成线的颜色
     */
    public HorizontalStepView setStepsViewIndicatorUnCompletedLineColor(int unCompletedLineColor) {
        mStepsViewIndicator.setUnCompletedLineColor(unCompletedLineColor);
        return this;
    }

    /**
     * 设置StepsViewIndicator完成线的颜色
     */
    public HorizontalStepView setStepsViewIndicatorCompletedLineColor(int completedLineColor) {
        mStepsViewIndicator.setCompletedLineColor(completedLineColor);
        return this;
    }

    /**
     * 设置StepsViewIndicator默认图片
     */
    public HorizontalStepView setStepsViewIndicatorDefaultIcon(Drawable defaultIcon) {
        mStepsViewIndicator.setDefaultIcon(defaultIcon);
        return this;
    }

    /**
     * 设置StepsViewIndicator已完成图片
     */
    public HorizontalStepView setStepsViewIndicatorCompleteIcon(Drawable completeIcon) {
        mStepsViewIndicator.setCompleteIcon(completeIcon);
        return this;
    }

    /**
     * 设置StepsViewIndicator正在进行中的图片
     */
    public HorizontalStepView setStepsViewIndicatorAttentionIcon(Drawable attentionIcon) {
        mStepsViewIndicator.setAttentionIcon(attentionIcon);
        return this;
    }

    /**
     * set textSize
     */
    public HorizontalStepView setTextSize(int textSize) {
        if (textSize > 0) {
            mTextSize = textSize;
        }
        return this;
    }

    @Override
    public void ondrawIndicator() {
        if (mTextContainer != null) {
            mTextContainer.removeAllViews();
            List<Float> complectedXPosition = mStepsViewIndicator.getCircleCenterPointPositionList();
            if (mStepBeanList != null && complectedXPosition != null && complectedXPosition.size() > 0) {
                for (int i = 0; i < mStepBeanList.size(); i++) {
                    TextView mTextView = new TextView(getContext());
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
                    mTextView.setText(mStepBeanList.get(i).getName());
                    int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    mTextView.measure(spec, spec);
                    // getMeasuredWidth
                    int measuredWidth = mTextView.getMeasuredWidth();
                    mTextView.setX(complectedXPosition.get(i) - measuredWidth / 2);
                    mTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    mTextView.setLineSpacing(10, 1.2f);
                    mTextView.setGravity(Gravity.CENTER);
                    StepBean stepBean = mStepBeanList.get(i);
                    String name = stepBean.getName();
                    int state = stepBean.getState();
                    SpannableString spanStr = new SpannableString(name);
                    String[] split = name.split("\\n");
                    if (i <= mComplectingPosition + 1) {
                        spanStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.grey_black_tv)), 0, split[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spanStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.grey_black_tv3)), split[0].length() + 1, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //mTextView.setTextColor(mComplectedTextColor);
                    } else {
                        spanStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), state == -1 ? R.color.grey_black_tv2 : R.color.grey_black_tv)), 0, split[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spanStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.grey_black_tv3)), split[0].length() + 1, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //mTextView.setTextColor(mUnComplectedTextColor);
                    }
                    mTextView.setText(spanStr);
                    mTextContainer.addView(mTextView);
                }
            }
        }
    }

}
