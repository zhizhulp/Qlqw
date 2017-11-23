package com.ascba.rebate.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.ModuleListAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.ModuleEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.ModulesUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jero on 2017/10/10 0010.
 */

public class ModulesAllActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private static final int GET = 305;
    private static final int CLICK = 992;

    private ModuleListAdapter moduleListAdapter;
    private List<ModuleEntity> homeList;
    private List<ModuleEntity> moduleList;

    @Override
    protected int bindLayout() {
        return R.layout.activity_modules_all;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        homeList = JSON.parseArray(getIntent().getStringExtra(ModulesUtils.HOME), ModuleEntity.class);
        fv(R.id.im_back).setOnClickListener(this);
        fv(R.id.module_search).setOnClickListener(this);
        moduleListAdapter = new ModuleListAdapter();
        moduleListAdapter.addHeaderView(getHeaderView());
        moduleListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ModuleEntity moduleEntity = (ModuleEntity) adapter.getItem(position);
                if (moduleEntity.getItemType() == 1) {
                    requestClick(moduleEntity.getNav_id());
                    ModulesUtils.itemGo(ModulesAllActivity.this, moduleEntity);
                }
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0)
                    return 4;
                return moduleListAdapter.getItem(position - 1).getItemType();
            }
        });
        mRecyclerView.setAdapter(moduleListAdapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        requestNetwork();
    }

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.item_module_top, null);
        view.findViewById(R.id.module_edit).setOnClickListener(this);
        setTopImg(view);
        return view;
    }

    private void setTopImg(View view) {
        int[] ids = new int[]{R.id.module_top_iv1, R.id.module_top_iv2,
                R.id.module_top_iv3, R.id.module_top_iv4,
                R.id.module_top_iv5, R.id.module_top_iv6, R.id.module_top_iv7};
        for (int i = 0; i < 6; i++) {
            if (homeList.size() - 1 > i)
                Picasso.with(this).load(homeList.get(i).getNav_icon())
                        .placeholder(R.mipmap.module_loading).into((ImageView) view.findViewById(ids[i]));
            else
                ((ImageView) view.findViewById(ids[i])).setImageResource(0);
        }
    }

    private void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.moreNav, RequestMethod.GET, null);
        executeNetwork(GET, "请稍后", request);
    }

    private void requestClick(int id) {
        AbstractRequest request = buildRequest(UrlUtils.navClick, RequestMethod.POST, null);
        request.add("nav_id", id);
        executeNetwork(CLICK, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        switch (what) {
            case GET:
                if (moduleList == null)
                    moduleList = new ArrayList<>();
                else
                    moduleList.clear();
                JSONObject jsonObject = JSON.parseObject(result.getData().toString());
//                JSONArray recommendArray = jsonObject.getJSONArray("recommend");
//                if (recommendArray != null && !recommendArray.isEmpty()) {
//                    moduleList.add(new ModuleEntity("为你推荐", ""));
//                    moduleList.addAll(JSON.parseArray(recommendArray.toJSONString(), ModuleEntity.class));
//                }
//                JSONArray recentlyArray = jsonObject.getJSONArray("recently");
//                if (recentlyArray != null && !recentlyArray.isEmpty()) {
//                    moduleList.add(new ModuleEntity("最近使用", ""));
//                    moduleList.addAll(JSON.parseArray(recentlyArray.toJSONString(), ModuleEntity.class));
//                }
                JSONArray listArray = jsonObject.getJSONArray("navCategory");
                ModuleEntity moduleEntity;
                JSONObject obj;
                JSONArray array;
                for (int i = 0; i < listArray.size(); i++) {
                    obj = listArray.getJSONObject(i);
                    array = obj.getJSONArray("nav");
                    if (!array.isEmpty()) {
                        moduleEntity = JSON.parseObject(obj.toJSONString(), ModuleEntity.class);
                        moduleEntity.setItemType(4);
                        moduleList.add(moduleEntity);
                        moduleList.addAll(JSON.parseArray(array.toJSONString(), ModuleEntity.class));
                    }
                }
                moduleListAdapter.setNewData(moduleList);
                break;
            case CLICK:
                break;
        }
    }

    @Override
    protected void mHandle404(int what, Result result) {
        if (what != CLICK) {
            super.mHandle404(what, result);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.module_search:
                startActivity(ModulesSearchActivity.class, null);
                break;
            case R.id.module_edit:
                gotoEdit();
                break;
        }
    }

    private void gotoEdit() {
        if (moduleList == null || moduleList.isEmpty())
            return;
        Bundle bundle = new Bundle();
        bundle.putString(ModulesUtils.LIST, JSON.toJSONString(moduleList));
        bundle.putString(ModulesUtils.HOME, JSON.toJSONString(homeList));
        startActivityForResult(ModulesEditActivity.class, bundle, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            setResult(RESULT_OK, data);
            homeList.clear();
            homeList.addAll(JSON.parseArray(data.getStringExtra(ModulesUtils.HOME), ModuleEntity.class));
            setTopImg(moduleListAdapter.getHeaderLayout());
        }
    }
}
