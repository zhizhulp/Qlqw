package com.ascba.rebate.activities.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.merchant.MctModBaseActivity;
import com.ascba.rebate.activities.merchant.MctTypeActivity;
import com.ascba.rebate.adapter.MctTypeAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.MctType;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.KeyBoardUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.shop.MoneyBar;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2018/1/29 11:16
 * Describe: 线上商品分类
 */

public class OnLineTypeActivity extends BaseDefaultNetActivity implements TextWatcher, AdapterView.OnItemClickListener {
    private EditText autoEt;
    private List<MctType> dataAll = new ArrayList<>();
    private List<MctType> filterData = new ArrayList<>();
    private MctTypeAdapter mctAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_on_line_type;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        autoEt = fv(R.id.actv);
        autoEt.addTextChangedListener(this);
        MoneyBar mbShop = fv(R.id.shop_mb);
        mbShop.setCallBack(mbShop.new CallbackImp() {
            @Override
            public void clickBack(View back) {
                KeyBoardUtils.showKeyboard(OnLineTypeActivity.this, autoEt, false);
                super.clickBack(back);
            }
        });
        ListView listView = fv(R.id.listView);
        mctAdapter = new MctTypeAdapter(filterData);
        listView.setAdapter(mctAdapter);
        listView.setOnItemClickListener(this);
        requestData();
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.getStoreTaglib, RequestMethod.GET, null);
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            String str = (String) result.getData();
            JSONObject jObj = JSON.parseObject(str);
            List<MctType> list = JSON.parseArray(jObj.getString("taglib"), MctType.class);
            dataAll.addAll(list);
            filterData.addAll(dataAll);
            mctAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MctType item = (MctType) mctAdapter.getItem(position);
        KeyBoardUtils.showKeyboard(OnLineTypeActivity.this, autoEt, false);
        Intent intent = getIntent();
        intent.putExtra("type", item.getText());
        intent.putExtra("type_id", item.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    public static void start(Activity activity, String type) {
        Intent intent = new Intent(activity, OnLineTypeActivity.class);
        intent.putExtra("type", type);
        activity.startActivityForResult(intent, CodeUtils.REQUEST_MCT_TYPE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (dataAll.size() > 0) {
            if ("".equals(s.toString())) {
                filterData.clear();
                filterData.addAll(dataAll);
                mctAdapter.notifyDataSetChanged();
            } else {
                findStrContainInput(s.toString());
            }
        }
    }

    private void findStrContainInput(String input) {
        filterData.clear();
        for (int i = 0; i < dataAll.size(); i++) {
            MctType mctType = dataAll.get(i);
            String text = mctType.getText();
            if (text != null) {
                if (text.contains(input)) {
                    filterData.add(mctType);
                }
            }
        }
        mctAdapter.notifyDataSetChanged();
    }

}
