package com.ascba.rebate.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;

/**
 * Created by lenovo on 2017/8/29.
 * 公共方法类
 */

public class CommonMethodUtils {

    public static void setClearStatus(EditText et, final ImageView iv) {
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    iv.setVisibility(View.VISIBLE);
                } else {
                    iv.setVisibility(View.GONE);
                }
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0) {
                    iv.setVisibility(View.VISIBLE);
                } else {
                    iv.setVisibility(View.GONE);
                }

            }
        });

    }

    public static String getMonthString(String mm, String yy) {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int severMonth = Integer.parseInt(mm);
        int severYear = Integer.parseInt(yy);
        if (year == severYear)
            if (severMonth == month)
                return "本月";
            else
                return severMonth + "月";
        else
            return severYear + "年" + severMonth + "月";
    }
}
