package com.ascba.rebate.activities.shop;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ScrollView;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;

public class ShopInActivity extends BaseDefaultNetActivity {

    private ScrollView scrollView;
    private EditText editText;

    @Override
    protected int bindLayout() {
        return R.layout.activity_shop_in;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        scrollView = fv(R.id.scrollView);
        editText = fv(R.id.editTextHint);
    }
}
