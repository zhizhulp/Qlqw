package com.ascba.rebate.activities.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ascba.rebate.R;


/**
 * Created by 李平 on 2017/9/12 16:01
 * Describe:仅供测试
 */

public class TestActivity extends AppCompatActivity {
    private String[] strs = new String[]{"开心每一天"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        addData(flipper);
    }

    private void addData(ViewFlipper flipper) {
        for (int i = 0; i < strs.length; i++) {
            TextView tv=new TextView(this);
            tv.setText(strs[i]);
            flipper.addView(tv);
        }
    }

}
