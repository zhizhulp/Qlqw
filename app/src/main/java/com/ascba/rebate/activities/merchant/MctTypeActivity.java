package com.ascba.rebate.activities.merchant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.MctTypeAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.MctType;
import com.ascba.rebate.utils.CodeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2017/12/4 9:58
 * Describe: 主营类目
 */

public class MctTypeActivity extends BaseDefaultNetActivity implements AdapterView.OnItemClickListener {
    private MctTypeAdapter adapter;
    private AutoCompleteTextView autoEt;
    private List<MctType> data;

    @Override
    protected int bindLayout() {
        return R.layout.activity_mct_type;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        autoEt = fv(R.id.actv);
        addData();
        adapter = new MctTypeAdapter(data);
        autoEt.setAdapter(adapter);
        autoEt.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text = data.get(position).getText();
        Intent intent = getIntent();
        intent.putExtra("type", text);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void addData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        for (int i = 0; i < 10; i++) {
            data.add(new MctType(i, "text" + i));
        }
    }

    public static void start(Activity activity, String type) {
        Intent intent = new Intent(activity, MctTypeActivity.class);
        intent.putExtra("type", type);
        activity.startActivityForResult(intent, CodeUtils.REQUEST_MCT_TYPE);
    }

}
