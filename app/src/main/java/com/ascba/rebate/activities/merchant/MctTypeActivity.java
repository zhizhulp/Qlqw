package com.ascba.rebate.activities.merchant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.MctTypeAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.MctType;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.List;

/**
 * Created by 李平 on 2017/12/4 9:58
 * Describe: 主营类目
 */

public class MctTypeActivity extends BaseDefaultNetActivity implements AdapterView.OnItemClickListener {
    private AutoCompleteTextView autoEt;

    @Override
    protected int bindLayout() {
        return R.layout.activity_mct_type;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        autoEt = fv(R.id.actv);
        requestData();
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.getSellerTaglib, RequestMethod.GET, null);
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            String str = (String) result.getData();
            JSONObject jObj = JSON.parseObject(str);
            List<MctType> list = JSON.parseArray(jObj.getString("taglib"), MctType.class);
            MctTypeAdapter adapter = new MctTypeAdapter(list);
            autoEt.setAdapter(adapter);
            autoEt.setVisibility(View.VISIBLE);
            autoEt.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text = ((MctType) autoEt.getAdapter().getItem(position)).getText();
        Intent intent = getIntent();
        intent.putExtra("type", text);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static void start(Activity activity, String type) {
        Intent intent = new Intent(activity, MctTypeActivity.class);
        intent.putExtra("type", type);
        activity.startActivityForResult(intent, CodeUtils.REQUEST_MCT_TYPE);
    }

}
