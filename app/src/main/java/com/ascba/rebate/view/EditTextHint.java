package com.ascba.rebate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;

import com.ascba.rebate.R;
import com.ascba.rebate.utils.ScreenDpiUtils;

/**
 * 可以自定义hint大小的EditText
 */

public class EditTextHint extends AppCompatEditText {
    public EditTextHint(Context context) {
        super(context);
    }


    public EditTextHint(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextHint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EditTextHint);
        float mpx = ScreenDpiUtils.sp2px(context, 14);//默认14sp
        float dimension = ta.getDimension(R.styleable.EditTextHint_hintSize, mpx);
        float msp = ScreenDpiUtils.px2sp(context, dimension);
        CharSequence hint = getHint();
        if(hint!=null){
            SpannableString spannableString = new SpannableString(getHint().toString());
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan((int) msp, false);
            spannableString.setSpan(absoluteSizeSpan , 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setHint(new SpannedString(spannableString)); // 一定要进行转换,否则属性只有一次
        }
        ta.recycle();
    }

}
