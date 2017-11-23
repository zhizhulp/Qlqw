package com.ascba.rebate.activities.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.ModuleSearchAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.ModuleEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.ModulesUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.ClearEditText;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.List;

/**
 * Created by Jero on 2017/10/10 0010.
 */

public class ModulesSearchActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private ModuleSearchAdapter moduleSearchAdapter;
    private List<ModuleEntity> moduleList;

    private ClearEditText searchEditText;

    @Override
    protected int bindLayout() {
        return R.layout.activity_modules_search;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        fv(R.id.im_back).setOnClickListener(this);
        moduleSearchAdapter = new ModuleSearchAdapter();
        moduleSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ModuleEntity moduleEntity = (ModuleEntity) adapter.getItem(position);
                if (moduleEntity.getItemType() == 1) {
                    requestClick(moduleEntity.getNav_id());
                    ModulesUtils.itemGo(ModulesSearchActivity.this, moduleEntity);
                }
            }
        });
        mRecyclerView.setAdapter(moduleSearchAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchEditText = fv(R.id.module_search);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0)
                    requestSearch(s.toString());
            }
        });

    }

    private void requestClick(int id) {
        AbstractRequest request = buildRequest(UrlUtils.navClick, RequestMethod.POST, null);
        request.add("nav_id", id);
        executeNetwork(0, "请稍后", request);
    }

    private void requestSearch(String search) {
        AbstractRequest request = buildRequest(UrlUtils.navSearch, RequestMethod.GET, null);
        request.add("keyword", search);
        executeNetwork(1, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 1) {
            moduleList = JSON.parseArray(JSON.parseObject(result.getData().toString()).getString("navList"), ModuleEntity.class);
            moduleSearchAdapter.setNewData(moduleList);
        }
    }

    @Override
    protected void mHandle404(int what, Result result) {
        if (what == 1) {
            if (moduleList != null)
                moduleList.clear();
            moduleSearchAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_back:
                finish();
                break;
        }
    }

}
